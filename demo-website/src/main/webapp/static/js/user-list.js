$(document).ready(function () {
    function userActions() {
        var template = "";
        if (hasPermission(['user.read'])) {
            template += "<button class='k-button k-primary'><a href='/admin/user/user/#:data.id#/view' class='k-link k-primary'>" + i18n('product.view') + "</a></button>";
        }
        if (hasPermission(['user.write'])) {
            template += "<button class='k-button k-primary'><a href='/admin/user/user/#:data.id#' class='k-link k-primary'>" + i18n('product.edit') + "</a></button><button class='k-button product-delete-btn' data-id='#:data.id#'>" + i18n('product.delete') + "</button>";
        }
        return template;
    }

    $("#grid").kendoGrid({
        dataSource: {
            type: "json",
            transport: {
                read: function (options) {
                    var data = {
                        username: null,
                        page: options.data.page,
                        limit: options.data.pageSize
                    };
                    if (options.data.sort && options.data.sort[0]) {
                        data.sortingField = options.data.sort[0].field;
                        data.desc = options.data.sort[0].dir === "desc";
                    }
                    $.ajax({
                        url: "/admin/api/user/find",
                        method: "PUT",
                        data: JSON.stringify(data),
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
                data: "items",
                total: "total",
                model: {
                    fields: {
                        updatedTime: {type: "date"}
                    }
                }
            },
            serverPaging: true,
            serverSorting: true,
            pageSize: 10
        },
        // groupable: true,
        sortable: true,
        pageable: {
            refresh: true,
            pageSizes: true,
            buttonCount: 5
        },
        columns: [{
            field: "username",
            title: i18n('user.name')
        }, {
            field: "email",
            title: i18n('user.email')
        }, {
            field: "updatedTime",
            title: i18n('user.updatedTime')
        }, {
            title: i18n('user.action'),
            width: "240px",
            template: userActions()
        }]
    });

    $(document).on("click", ".user-delete-btn", function (event) {
        var id = $(event.currentTarget).data("id");
        $('#dialog').kendoDialog({
            width: "450px",
            title: "Delete User",
            closable: true,
            modal: false,
            content: "<p>Would you like to delete it?<p>",
            actions: [
                {text: 'Cancel', action: onClose},
                {
                    text: 'Delete', primary: true, action: function () {
                        onDelete(id);
                    }
                }
            ]
        });
        $('#dialog').data("kendoDialog").open();
    });

    function onDelete(id) {
        var data = {
            ids: [id]
        };
        $.ajax({
            url: "/admin/api/user/batch-delete",
            method: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (result) {
                window.location.reload();
            },
            error: function (result) {
                $('#notification').kendoNotification().data("kendoNotification").show("delete error", "error");
            }
        });
        onClose();
    }

    function onClose() {
        $('#dialog').data("kendoDialog").close();

    }

});