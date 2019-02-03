window.i18n = function(key) {
    function message(obj, prop) {
        if (typeof obj === 'undefined') {
            return false;
        }
        var index = prop.indexOf('.')
        if (index > -1) {
            return message(obj[prop.substring(0, index)], prop.substr(index + 1));
        }
        return obj[prop];
    }

    return message(window.messages, key);
};

window.hasPermission = function(permissions) {
    for (var i = 0; i < permissions.length; i++) {
        var permission = permissions[i];
        var userPermissions = window.user.permissions;
        var hasPermission = false;
        for (var j = 0; j < userPermissions.length; j++) {
            if (userPermissions[j] === permission) {
                hasPermission = true;
                break;
            }
        }
        if (!hasPermission) {
            return false;
        }
    }
    return true;
};