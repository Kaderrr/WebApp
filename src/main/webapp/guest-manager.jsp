<%--suppress HtmlUnknownTarget --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<c:choose>
    <c:when test="${empty sessionScope.user_id || sessionScope.user_permission_level.toLowerCase() == 'service_provider'}">
        <html>
        <head>
            <title>Unauthorized</title>
        </head>
        <body>
        <h1>Private User Area</h1>
        <h2>Unauthorized access.</h2>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <html lang="en" class="h-100">
        <head>
            <!-- Required meta tags -->
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">

            <!-- Bootstrap CSS -->
            <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link rel="icon" type="image/x-icon" href="images/favicon.ico">
            <link href="css/common.css" rel="stylesheet">
            <title>Guest manager</title>
        </head>
        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-guests">
            <!-- NAV -->
        </div>
        <div class="container">
            <div class="row my-5">
                <h1 class="display-4 text-center">Guest management</h1>
            </div>
            <div class="row g-3 align-items-center text-center no-print">
                <div class="col-md-6">
                    <button type="button" class="btn btn-primary btn-lg" data-bs-toggle="modal"
                            data-bs-target="#add-guest-modal">Add guest
                    </button>
                </div>
                <div class="col-md-6">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="window.print()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-printer-fill" viewBox="0 0 16 16">
                            <path d="M5 1a2 2 0 0 0-2 2v1h10V3a2 2 0 0 0-2-2H5zm6 8H5a1 1 0 0 0-1 1v3a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-3a1 1 0 0 0-1-1z"></path>
                            <path d="M0 7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-1v-2a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2H2a2 2 0 0 1-2-2V7zm2.5 1a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1z"></path>
                        </svg>
                    </button>
                </div>
            </div>

            <hr/>
            <form id="form-filter">
                <div class="row g-3 mb-5 align-items-end">
                    <div class="col-sm-3">
                        <label for="filter-select-type" class="form-label">Filter type</label>
                        <select id="filter-select-type" class="form-select" aria-label="Type select filter">
                            <option selected value="">No filter</option>
                            <option value="normal">Normal</option>
                            <option value="premium">Premium</option>
                            <option value="disabled">Disabled</option>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <label for="filter-input-name" class="form-label">Filter name</label>
                        <input id="filter-input-name" type="text" class="form-control" placeholder="ex. Mario"
                               aria-label="Name Filter">
                    </div>
                    <div class="col-sm-3">
                        <label for="filter-input-surname" class="form-label">Filter surname</label>
                        <input id="filter-input-surname" type="text" class="form-control" placeholder="ex. Rossi"
                               aria-label="Surname Filter">
                    </div>
                    <div class="col-sm text-center">
                        <button type="submit" class="btn btn-primary">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor"
                                 class="bi bi-funnel-fill"
                                 viewBox="0 0 16 16">
                                <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5v-2z"></path>
                            </svg>
                        </button>
                    </div>
                </div>
            </form>
            <form id="form-action-on-selected">
                <div class="row mb-4 text-center align-items-center no-print">
                    <div class="col-sm-3">
                        With selected:
                    </div>
                    <div class="col-sm-3">
                        <select required id="select-action" class="form-select form-select-sm"
                                aria-label="selected action">
                            <option selected disabled value="">Select action</option>
                            <option value="delete">Delete</option>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <button class="btn btn-sm btn-outline-danger" type="submit">Execute</button>
                    </div>
                </div>
                <div class="row">
                    <div class="table-responsive text-nowrap table-container-size" id="table-container">
                        <table class="table table-striped" id="table-guests">
                            <thead>
                            <tr>
                                <th class="no-print" scope="col"></th>
                                <th scope="col">Id</th>
                                <th scope="col">Name</th>
                                <th scope="col">Surname</th>
                                <th scope="col">Type</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td colspan="5" class="text-center">
                                    No data
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
        </div>
        <div id="add-guest-modal" class="modal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form id="form-add-guest" action="${pageContext.request.contextPath}/rest/guest/insert-one"
                          method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Add a guest</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row mb-3">
                                <label for="add-guest-id" class="col-sm-2 col-form-label">Id</label>
                                <div class="col-sm-10">
                                    <input required name="id" type="text" class="form-control" id="add-guest-id">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label for="add-guest-name" class="col-sm-2 col-form-label">Name</label>
                                <div class="col-sm-10">
                                    <input required name="name" type="text" class="form-control" id="add-guest-name">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label for="add-guest-surname" class="col-sm-2 col-form-label">Surname</label>
                                <div class="col-sm-10">
                                    <input required name="surname" type="text" class="form-control"
                                           id="add-guest-surname">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label for="add-guest-type" class="col-sm-2 col-form-label">Type</label>
                                <div class="col-sm-10">
                                    <select required name="type" id="add-guest-type" class="form-select form-select-sm"
                                            aria-label="select type">
                                        <option selected disabled value="">Select type</option>
                                        <option value="normal">Normal</option>
                                        <option value="disabled">Disabled</option>
                                        <option value="premium">Premium</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div data-autoload="templates/footer.html footer">
            <!-- FOOTER -->
        </div>
        <script src="js/jquery-3.6.0.min.js"></script>
        <script src="js/jquery.serializejson.min.js"></script>
        <script src="js/contextPath.js"></script>
        <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="js/autoloadLib.js"></script>
        <script src="js/common.js"></script>
        <script src="js/guest-manager.js"></script>
        </body>
        </html>
    </c:otherwise>
</c:choose>
