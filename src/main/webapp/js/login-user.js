function loginResultHandlerUser(resultJson, formOutput) {
    if (!resultJson.status) {
        formOutput.css("color", "red");
        formOutput.html(resultJson.statusMessage);
    } else {
        formOutput.css("color", "green");
        formOutput.html("Login succeeded.");
        secondsDelayRedirect(contextPath + "/services.jsp", 1);
    }
}

function loginFormUserRequestUser(form, formOutput) {
    createFormAjaxRequest(
        form,
        (result) => {
            loginResultHandlerUser(result, formOutput);
        }
    );
}

$(() => {
    let formOutput = $("#form-login-user-output");
    $("#form-login-user").on("submit", (e) => {
        e.preventDefault();
        loginFormUserRequestUser(e.target, formOutput);
    });
});
