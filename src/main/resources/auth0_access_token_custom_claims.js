function (user, context, callback) {
    if (context.clientID === '******') {
        var namespace = 'http://johnhunsley.events.com/';
        context.accessToken[namespace+'organisation'] = user.app_metadata.organisation;
        context.accessToken[namespace+'firstName'] = user.user_metadata.firstName;
        context.accessToken[namespace+'lastName'] = user.user_metadata.lastName;
        context.accessToken[namespace+'phoneNumber'] = user.user_metadata.phoneNumber;
        context.accessToken.scope = user.app_metadata.role;
    }


    callback(null, user, context);
}