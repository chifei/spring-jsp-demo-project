$(document).ready(function() {

    var validator = $("#login-form").kendoValidator().data("kendoValidator"),
        status = $(".status");

    $("form").submit(function(event) {
        event.preventDefault();
        if (!validator.validate()) {
            return;
        }
        var array =  $("form").serializeArray(),
            object = {},
            i = 0;
        for (; i < array.length; i += 1) {
            var data = array[i];
            object[data.name] = data.value;
        }

        $.ajax({
            url: "/admin/api/user/login",
            method: "POST",
            data: JSON.stringify(object),
            contentType: "application/json",
            dataType: "json"
        }).then(function (loginResponse) {
            if (loginResponse.success) {
                window.location.href = loginResponse.fromURL;
            } else {
                status.text(loginResponse.message)
                    .removeClass("k-text-success")
                    .addClass("k-text-error");
            }
        }).fail(function (result) {
            if (result) {
                status.text(result.message)
                    .removeClass("k-text-success")
                    .addClass("k-text-error");
            }
        });
    });
});