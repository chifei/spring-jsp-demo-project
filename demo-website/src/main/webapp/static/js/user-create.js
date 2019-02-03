$(document).ready(function () {
    $("#roleIds").kendoMultiSelect({
        placeholder: "Select roles...",
        dataTextField: "name",
        dataValueField: "id",
        autoBind: false,
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: function (options) {
                    $.ajax({
                        url: "/admin/api/user/role/find",
                        method: "PUT",
                        data: JSON.stringify({}),
                        dataType: "json",
                        contentType: "application/json",
                        success: function (result) {
                            options.success(result);
                        },
                        error: function (result) {
                            options.error(result);
                        }
                    });
                }
            },
            schema: {
                data: "items"
            }
        }
    });
    var validator = $("form").kendoValidator().data("kendoValidator"),
        status = $(".status");
    $(".tc-activator").click(function () {
        if (!validator.validate()) {
            return;
        }
        var array = $("form").serializeArray(),
            object = {},
            i = 0;
        for (; i < array.length; i += 1) {
            var data = array[i];
            object[data.name] = data.value;
        }
        object.roleIds = $("#roleIds").val();
        $.ajax({
            url: "/admin/api/user",
            method: "post",
            data: JSON.stringify(object),
            dataType: "json",
            contentType: "application/json"
        }).then(function (response) {
            window.location.href = "/admin/user/user";
        });
    });
});