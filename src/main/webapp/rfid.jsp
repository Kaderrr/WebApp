<%--suppress HtmlUnknownTarget --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
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

            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link rel="icon" type="image/x-icon" href="images/favicon.ico">
            <link href="css/common.css" rel="stylesheet">
            <title>RFID</title>
        </head>
        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-bands">
            <!-- NAV -->
        </div>
        <div class="container">
            <div class="row my-5">
                <h1 class="display-4 text-center">RFID list</h1>
            </div>
            <hr/>
            <div class="row g-3 align-items-center no-print">
                <div class="col-md-6">
                    <button type="button" class="btn btn-primary btn-lg" data-bs-toggle="modal"
                            data-bs-target="#modal-insert-rfid">Create RFID
                    </button>
                </div>
                <div class="col-md-6 text-end">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="window.print()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-printer-fill" viewBox="0 0 16 16">
                            <path d="M5 1a2 2 0 0 0-2 2v1h10V3a2 2 0 0 0-2-2H5zm6 8H5a1 1 0 0 0-1 1v3a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-3a1 1 0 0 0-1-1z"></path>
                            <path d="M0 7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-1v-2a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2H2a2 2 0 0 1-2-2V7zm2.5 1a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1z"></path>
                        </svg>
                    </button>
                </div>
            </div>
            <hr>
            <form id="filter-delete-form">
                <div class="header-form-wrapper container-fluid">
                    <div class="row align-items-center">
                        <div class="col-12 col-md-6 col-lg-4 select-wrapper input-field mt-3 mt-lg-0">
                            <select id="select-filter-type" class="form-select" aria-label="Select rfid type">
                                <option selected value="">Select Type</option>
                                <option value="band">Band</option>
                                <option value="card">Card</option>
                            </select>
                        </div>
                        <div class="col-12 col-md-6 col-lg-4 select-wrapper input-field mt-3 mt-lg-0">
                            <select id="select-filter-status" class="form-select" aria-label="Select rfid status">
                                <option selected value="">Select Status</option>
                                <option value="valid">Valid</option>
                                <option value="lost">Lost</option>
                                <option value="broken">Broken</option>
                            </select>
                        </div>
                        <div class="col-12 col-md-12 col-lg-4 mt-3 mt-lg-0 text-center">
                            <button id="btn-filter" type="button" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="55" height="25" fill="currentColor"
                                     class="bi bi-funnel-fill"
                                     viewBox="0 0 16 16">
                                    <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5v-2z"></path>
                                </svg>
                            </button>
                            <button id="btn-reset" type="reset" class="btn btn-dark">Reset</button>
                        </div>
                    </div>
                </div>
            </form>
            <hr>

            <form id="form-action-on-selected">
                <div class="row mb-4 text-center align-items-center no-print">
                    <div class="col-sm">
                        With selected:
                    </div>
                    <div class="col-sm">
                        <select required id="select-action" class="form-select form-select-sm"
                                aria-label="selected action">
                            <option selected disabled value="">Select action</option>
                            <option value="delete">Delete</option>
                        </select>
                    </div>
                    <div class="col-sm mt-2 mt-md-0">
                        <button class="btn btn-sm btn-outline-danger" type="submit">Execute</button>
                    </div>
                </div>
                <div class="row mt-5">
                    <div class="col-sm-10 col-md-10 col-lg-12">
                        <div class="table-responsive text-nowrap table-container-size">
                            <table class="table table-striped" id="rfids-table">
                                <thead>
                                <tr>
                                    <th scope="col">Select</th>
                                    <th scope="col">RFID Number</th>
                                    <th scope="col">Type</th>
                                    <th scope="col">Status</th>
                                </tr>
                                </thead>
                                <tbody id="table body">

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </form>


            <div class="modal fade" id="modal-insert-rfid" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Create/Edit RFID</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                    aria-label="Cancel"></button>
                        </div>
                        <div class="modal-body row">
                            <form id="form-rfid-add" action="${pageContext.request.contextPath}/rest/rfid/insert-one"
                                  method="post">
                                <div class="row mb-3">
                                    <label for="input-rfid-id" class="col-sm-2 col-form-label">RFID Number:</label>
                                    <div class="col-sm-10">
                                        <input required name="code" class="form-control form-select-sm"
                                               id="input-rfid-id">
                                    </div>
                                </div>
                                <div class="row mb-3 ">
                                    <div class="col-sm">
                                        <label for="select-rfid-type">Type:</label>
                                        <select required name="type" id="select-rfid-type" class="form-select"
                                                aria-label="Select rfid type">
                                            <option selected disabled value="">Select type</option>
                                            <option value="card">Card</option>
                                            <option value="band">Band</option>
                                        </select>
                                    </div>
                                    <div class="col-sm">
                                        <label for="select-rfid-status">Status:</label>
                                        <select required name="status" id="select-rfid-status"
                                                class="form-select" aria-label="Select rfid status">
                                            <option selected disabled value="">Select status</option>
                                            <option value="valid">Valid</option>
                                            <option value="lost">Lost</option>
                                            <option value="broken">Broken</option>
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" form="form-rfid-add" class="btn btn-primary">Save</button>
                        </div>
                    </div>
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
        <script src="js/common.js"></script>
        <script src="js/autoloadLib.js"></script>
        <script src="js/create-rfid.js"></script>
        </body>
        </html>
    </c:otherwise>
</c:choose>
