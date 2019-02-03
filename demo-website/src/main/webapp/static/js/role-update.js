$(document).ready(function () {
    $("#permissions").kendoMultiSelect({
        placeholder: "Select permissions...",
        dataTextField: "displayName",
        dataValueField: "name",
        autoBind: false,
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: function (options) {
                    $.ajax({
                        url: "/admin/api/user/permissions",
                        method: "GET",
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
            }
        }
    });
    var permissions = $("#selected-permissions").val();
    if (permissions) {
        permissions = permissions.replace("[", "").replace("]", "").split(",");
        var values = [];
        for (var i = 0; i < permissions.length; i += 1) {
            values.push(permissions[i].trim());
        }
        var multiSelect = $("#permissions").data("kendoMultiSelect");
        multiSelect.value(values);
    }
 

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
        object.permissions = $("#permissions").val();
        $.ajax({
            url: "/admin/api/user/role/" + $("#id").val(),
            method: "put",
            data: JSON.stringify(object),
            dataType: "json",
            contentType: "application/json"
        }).then(function (response) {
            window.location.href = "/admin/user/role";
        });
    });
});