$(document).ready(function () {
    function roleActions() {
        var template = "";
        if (hasPermission(['role.read'])) {
            template += "<button class='k-button k-primary'><a href='/admin/user/role/#:data.id#/view' class='k-link k-primary'>" + i18n('product.view') + "</a></button>";
        }
        if (hasPermission(['role.write'])) {
            template += "<button class='k-button k-primary'><a href='/admin/user/role/#:data.id#' class='k-link k-primary'>" + i18n('product.edit') + "</a></button><button class='k-button product-delete-btn' data-id='#:data.id#'>" + i18n('product.delete') + "</button>";
        }
        return template;
    }

    $("#grid").kendoGrid({
        dataSource: {
            type: "json",
            transport: {
                read: function (options) {
                    var data = {
                        name: null,
                        page: options.data.page,
                        limit: options.data.pageSize
                    };
                    if (options.data.sort && options.data.sort[0]) {
                        data.sortingField = options.data.sort[0].field;
                        data.desc = options.data.sort[0].dir === "desc";
                    }
                    $.ajax({
                        url: "/admin/api/user/role/find",
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
                parse: function (response) {
                    console.log(response);
                    var items = response.items;
                    for (var i = 0; i < items.length; i += 1) {
                        items[i].permissionStr = "[" + items[i].permissions.join(",") + "]";
                    }
                    return response;
                },
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
            field: "name",
            title: i18n("user.name")
        }, {
            field: "permissionStr",
            title: i18n("user.permission")
        }, {
            field: "status",
            title: i18n("user.status")
        }, {
            field: "updatedTime",
            title: i18n("user.updatedTime")
        }, {
            title: i18n("user.action"),
            width: "240px",
            template: roleActions()
        }]
    });

    $(document).on("click", ".role-delete-btn", function (event) {
        var id = $(event.currentTarget).data("id");
        $('#dialog').kendoDialog({
            width: "450px",
            title: "Delete Role",
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
            url: "/admin/api/user/role/batch-delete",
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