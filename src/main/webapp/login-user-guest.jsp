<%--suppress ALL --%>
<%--suppress HtmlUnknownTarget --%>
<html lang="en" class="h-100">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/login.css" rel="stylesheet">
    <link href="./css/common.css" rel="stylesheet">
    <link rel="icon" type="image/x-icon" href="images/favicon.ico">

    <title>Login User / Guest</title>
</head>
<body class="d-flex flex-column h-100">
<a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
<div data-autoload="templates/nav.html nav" data-setactive="#nav-login" data-hidelogout="true">
    <!-- NAV -->
</div>

<div class="container login-container">
    <div class="row">
        <div class="col-md-6 login-form-1" style="margin-bottom: 10px">
            <form id="form-login-user" action="${pageContext.request.contextPath}/rest/auth/user/login" method="post">

                <h3>User Login</h3>

                <div class="form-group">
                    <input name="email" type="email" class="form-control" placeholder="Your Email *" value=""/>
                </div>
                <div class="form-group">
                    <input name="password" type="password" class="form-control" placeholder="Your Password *" value=""/>
                </div>
                <div class="form-group submit-centered">
                    <input type="submit" class="btnSubmit" value="Login"/>
                </div>
            </form>
            <p id="form-login-user-output"></p>
        </div>

        <div class="col-md-6 login-form-2" style="margin-bottom: 10px">

            <div class="login-logo d-sm-none d-md-block">
                <img src="images/logo.jpeg" alt=""/>
            </div>
            <form id="form-login-guest" action="${pageContext.request.contextPath}/rest/auth/guest/login" method="post">
                <h3>Guest Login</h3>
                <div class="form-group">
                    <input name="code" type="text" class="form-control" placeholder="Your RFID code *" value=""/>
                </div>
                <div class="form-group">
                    <input name="surname" type="text" class="form-control" placeholder="Your Surname *" value=""/>
                </div>
                <div class="form-group submit-centered">
                    <input type="submit" class="btnSubmit" value="Login"/>
                </div>
            </form>
            <p id="form-login-guest-output"></p>
        </div>
    </div>
</div>
<div data-autoload="templates/footer.html footer">
    <!-- FOOTER -->
</div>
<!--JS-->
<script src="js/jquery-3.6.0.min.js"></script>
<script src="js/jquery.serializejson.min.js"></script>
<script src="js/autoloadLib.js"></script>
<script src="bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="js/contextPath.js"></script>
<script src="js/common.js"></script>
<script src="js/login-user.js"></script>
<script src="js/login-guest.js"></script>
</body>
</html>
