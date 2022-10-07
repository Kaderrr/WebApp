function loginResultHandlerGuest(resultJson, formOutput) {
    if (!resultJson.status) {
        formOutput.css("color", "red");
        formOutput.html(resultJson.statusMessage);
    } else {
        formOutput.css("color", "green");
        formOutput.html("Login succeeded.");
        secondsDelayRedirect(contextPath + "/guest-view.jsp", 1);
    }
}

function loginFormGuestRequestGuest(form, formOutput) {
    createFormAjaxRequest(
        form,
        (result) => {
            loginResultHandlerGuest(result, formOutput);
        }
    );
}

$(() => {
    let formOutput = $("#form-login-guest-output");

    $("#form-login-guest").on("submit", (e) => {
        e.preventDefault();
        loginFormGuestRequestGuest(e.target, formOutput);
    });
});
