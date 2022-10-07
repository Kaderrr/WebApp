<%--suppress ALL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--suppress HtmlUnknownTarget --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:choose>
    <c:when test="${empty sessionScope.guest_id}">
        <html>
        <head>
            <title>Unauthorized</title>
        </head>
        <body>
        <h1>Private Guest Area Test</h1>
        <h2>Unauthorized access.</h2>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <!DOCTYPE html>
        <html lang="en" class="h-100">
        <head>
            <meta charset="UTF-8">
            <title>Guest View</title>
            <!-- Required meta tags -->
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">


            <!-- Bootstrap CSS -->
            <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link href="css/common.css" rel="stylesheet">
            <link href="css/guest-view.css" rel="stylesheet">
            <link rel="icon" type="image/x-icon" href="images/favicon.ico">

        </head>
        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
        <a style="display: none" id="guest-id">${sessionScope.guest_id}</a>
        <!--THE TABLE-->
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-guests">
            <!-- NAV -->
        </div>

        <div class="container">
            <div class="row mt-5">
                <div class="col-sm-1"></div>
                <div class="col-sm-1 centered"><img src="images/avatar.png" class="rounded-circle" style="width: 70px;"
                                                    alt="Avatar"/></div>
                <div class="col-sm-3 centered">
                    <h5>Name Surname: <span class="data-color output-guest-name"></span></h5>
                </div>
                <div class="col-sm-3 centered">
                    <h5>Room Number: <span class="data-color output-room-number"></span></h5>
                </div>
                <div class="col-sm-3 centered">
                    <h5>Balance: <span class="data-color output-balance"></span></h5>
                </div>
                <div class="col-sm-1 text-center">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="window.print()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-printer-fill" viewBox="0 0 16 16">
                            <path d="M5 1a2 2 0 0 0-2 2v1h10V3a2 2 0 0 0-2-2H5zm6 8H5a1 1 0 0 0-1 1v3a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-3a1 1 0 0 0-1-1z"/>
                            <path d="M0 7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-1v-2a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2H2a2 2 0 0 1-2-2V7zm2.5 1a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1z"/>
                        </svg>
                    </button>
                </div>
            </div>
            <form id="form-filter">
                <div class="row mt-5 text-center align-items-end">
                    <div class="col-md-3 mt-2 mt-md-0">
                        <label for="filter-date-start" class="form-label">Start date</label>
                        <input id="filter-date-start" type="date" class="form-control">
                    </div>
                    <div class="col-md-3 mt-2 mt-md-0">
                        <label for="filter-date-end" class="form-label">End date</label>
                        <input id="filter-date-end" type="date" class="form-control">
                    </div>
                    <div class="col-md-2 mt-2 mt-md-0">
                        <label for="select-filter-type" class="form-label">Filter type</label>
                        <select id="select-filter-type" class="form-select form-select-sm" aria-label="Dropdown type">
                            <option selected value="">No filter</option>
                            <option value="deposit">Deposit</option>
                            <option value="payment">Payment</option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2 mt-md-0">
                        <label for="select-filter-state" class="form-label">Filter state</label>
                        <select id="select-filter-state" class="form-select form-select-sm" aria-label="Dropdown state">
                            <option selected value="">No filter</option>
                            <option value="false">Deleted</option>
                            <option value="true">Successful</option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2 mt-md-0">
                        <button type="submit" class="btn btn-danger">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-funnel-fill" viewBox="0 0 16 16">
                                <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5v-2z"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </form>

            <div class="row mt-5">
                <div class="table-responsive text-nowrap">
                    <!--Table-->
                    <table id="table-details" class="table table-striped">
                        <!--Table head-->
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>State</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Date</th>
                            <th>Username</th>
                        </tr>
                        </thead>
                        <!--Table head-->

                        <!--Table body-->
                        <tbody>
                        <tr>
                            <th scope="row" colspan="7" class="text-center">No data</th>
                        </tr>
                        </tbody>
                        <!--Table body-->
                    </table>
                    <!--Table-->
                </div>
            </div>
        </div>

        <div data-autoload="templates/footer.html footer">
            <!-- FOOTER -->
        </div>

        <script src="js/jquery-3.6.0.min.js"></script>
        <script src="js/jquery.serializejson.min.js"></script>
        <script src="js/autoloadLib.js"></script>
        <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="js/contextPath.js"></script>
        <script src="js/common.js"></script>
        <script src="js/guest-view.js"></script>
        </body>
        </html>
    </c:otherwise>
</c:choose>
