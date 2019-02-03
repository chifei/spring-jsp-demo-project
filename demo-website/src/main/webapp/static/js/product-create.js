$(document).ready(function() {
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
        $.ajax({
            url: "/admin/api/product",
            method: "post",
            data: JSON.stringify(object),
            dataType: "json",
            contentType: "application/json"
        }).then(function (response) {
            window.location.href = "/admin/product";
        });
    });
});