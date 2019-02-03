# Spring JSP Demo Project

This demo uses Java 8, Gradle 5, and the latest stable release of Spring MVC, Spring Cache, Spring Messaging and Spring data. It also uses the kendo UI. 

## Try the demo

[https://demo4.jweb.app/](https://demo4.jweb.app/)<br>
admin/admin

> Database will be reset every 10 minutes.


## Features

- User login/logout
- User switch language (i18n for English and Chinese)
- User search user by role or name
- User create or update another user, assign roles.
- User delete another user with batch delete support
- User search role by name
- User create or update another role, assign permissions.
- User delete another role with batch delete support.
- User can only see buttons/menus with permission granted.
- Upload products with CSV files.
- Export products to CSV files.


## ACL implementation

- @LoginRequired and @PermissionRequired
- Corresponding LoginRequiredInterceptor and PermissionRequiredInterceptor
- React PermissionRequired Component

```java

@RequestMapping(value = "/admin/api/user/find", method = RequestMethod.PUT)
    @PermissionRequired("user.read")
    public UserQueryResponse find(@RequestBody UserQuery userQuery) {
        UserQueryResponse userQueryResponse = new UserQueryResponse();
        userQueryResponse.items = items(userService.find(userQuery));
        userQueryResponse.page = userQuery.page;
        userQueryResponse.limit = userQuery.limit;
        userQueryResponse.total = userService.count(userQuery);
        return userQueryResponse;
    }

    @RequestMapping(value = "/admin/api/user", method = RequestMethod.POST)
    @PermissionRequired("user.write")
    public UserResponse create(@RequestBody CreateUserRequest request) {
        request.requestBy = userInfo.username();
        return response(userService.create(request));
    }

```

```jsp
<j:hasPermission permission="user.write">
    <div class="themechooser" data-role="details">
        <a href="/admin/user/user/create" class="k-link k-button k-primary tc-activator"><spring:message code="user.createUser"/></a>
    </div>
</j:hasPermission>
```