function removeTrailingSlash(str) {
    return str.endsWith('/') ? str.slice(0, -1) : str;
}

function createFormAjaxRequest(form, whenDoneCallback, whenFailCallback) {
    let formJquery = $(form);
    let dataToSend = formJquery.serializeJSON();
    let method = formJquery.attr("method");
    let url = removeTrailingSlash(formJquery.attr("action"));
    let request;

    switch (method.toUpperCase()) {
        case "POST":
            request = $.ajax({
                url: url,
                method: method,
                data: JSON.stringify(dataToSend),
                dataType: "json",
                contentType: "application/json"
            });
            break;
        case "GET":
        default:
            console.log("Type of request not supported. Add it in the common.js file!")
            break;
    }

    if (!request) {
        return;
    }

    if (whenDoneCallback) {
        request.done(whenDoneCallback);
    }
    if (whenFailCallback) {
        request.fail(whenFailCallback);
    }
}

function secondsDelayRedirect(url, seconds) {
    setTimeout(
        () => {
            window.location.href = url
        },
        seconds * 1000
    )
}

function logout(who) {
    request = $.ajax({
        url: contextPath + "/rest/auth/" + who + "/logout",
        method: "POST",
        dataType: "json"
    }).done((response) => {
        if (response.status) {
            secondsDelayRedirect(contextPath + "/login-user-guest.jsp");
        } else {
            console.log("You are not logged in as " + who);
        }
    });
}

function logoutUser() {
    logout("user");
}

function logoutGuest() {
    logout("guest");
}

$(() => {
    console.log($(this));
    $(".logout-button").on("click", (event) => {


    });
});
