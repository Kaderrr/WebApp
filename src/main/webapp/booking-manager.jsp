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
            <!-- Required meta tags -->
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">

            <!-- Bootstrap CSS -->
            <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link rel="icon" type="image/x-icon" href="images/favicon.ico">
            <link href="css/common.css" rel="stylesheet">
            <title>Booking manager</title>
        </head>

        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>

        <!-- NAV -->
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-bookings"></div>

        <!-- CREATE -->
        <div class="container">

            <!-- HEADING -->
            <div class="row my-5">
                <h1 class="display-4 text-center">Booking management</h1>
            </div>

            <!-- CREATE BOOKING BUTTON AND PRINT BUTTON -->
            <div class="row g-3 align-items-center no-print">
                <div class="col-md-11">
                    <button type="button" class="btn btn-primary btn-lg" data-bs-toggle="modal"
                            data-bs-target="#add-booking-modal">Add Booking
                    </button>
                    <button type="button" class="btn btn-primary btn-lg" id="btn-add-match" data-bs-toggle="modal"
                            data-bs-target="#add-match-modal">Add Match
                    </button>
                </div>
                <div class="col-md-1">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="window.print()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-printer-fill" viewBox="0 0 16 16">
                            <path d="M5 1a2 2 0 0 0-2 2v1h10V3a2 2 0 0 0-2-2H5zm6 8H5a1 1 0 0 0-1 1v3a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-3a1 1 0 0 0-1-1z"></path>
                            <path d="M0 7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-1v-2a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2H2a2 2 0 0 1-2-2V7zm2.5 1a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1z"></path>
                        </svg>
                    </button>
                </div>
            </div>
            <hr>

            <!-- FILTER FORM -->
            <form id="filter-delete-booking-form">
                <div class="header-form-wrapper">
                    <div class="row align-items-end">

                        <!-- DATE PICKER FILTER -->
                        <div class="col-12 col-md-6 col-lg-3">
                            <label for="filter-checkin" class="form-label">Check-in</label>
                            <input id="filter-checkin" class="form-control" type="date"/>
                        </div>
                        <div class="col-12 col-md-6 col-lg-3">
                            <label for="filter-checkout" class="form-label">Check-out</label>
                            <input id="filter-checkout" class="form-control" type="date"/>
                        </div>

                        <!-- SELECT GUEST FILTER -->
                        <div class="col-12 col-md-6 col-lg-2">
                            <label for="select-filter-guest" class="form-label">Guest</label>
                            <select id="select-filter-guest" class="form-select">
                                <option selected value="">No filter</option>
                            </select>
                        </div>

                        <!-- SELECT ROOM FILTER -->
                        <div class="col-12 col-md-6 col-lg-2">
                            <label for="select-filter-room" class="form-label">Room</label>
                            <select id="select-filter-room" class="form-select" data-live-search="true"
                                    aria-label="Select the room">
                                <option selected value="">No filter</option>
                                <option value="A1">A1</option>
                                <option value="A2">A2</option>
                                <option value="A3">A3</option>
                                <option value="A4">A4</option>
                                <option value="A5">A5</option>
                                <option value="A6">A6</option>
                                <option value="A7">A7</option>
                                <option value="A8">A8</option>
                                <option value="A9">A9</option>
                                <option value="A10">A10</option>
                            </select>
                        </div>

                        <!-- FILTER AND DELETE BUTTONS -->

                        <div class="text-center col-12 col-lg-2 mt-3 mt-lg-0">
                            <button type="submit" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-funnel-fill" viewBox="0 0 16 16">
                                    <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5v-2z"></path>
                                </svg>
                            </button>
                            <button type="button" class="btn btn-outline-warning" id="reset-filter-button">
                                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-arrow-repeat" viewBox="0 0 16 16">
                                    <path d="M11.534 7h3.932a.25.25 0 0 1 .192.41l-1.966 2.36a.25.25 0 0 1-.384 0l-1.966-2.36a.25.25 0 0 1 .192-.41zm-11 2h3.932a.25.25 0 0 0 .192-.41L2.692 6.23a.25.25 0 0 0-.384 0L.342 8.59A.25.25 0 0 0 .534 9z"></path>
                                    <path fill-rule="evenodd" d="M8 3c-1.552 0-2.94.707-3.857 1.818a.5.5 0 1 1-.771-.636A6.002 6.002 0 0 1 13.917 7H12.9A5.002 5.002 0 0 0 8 3zM3.1 9a5.002 5.002 0 0 0 8.757 2.182.5.5 0 1 1 .771.636A6.002 6.002 0 0 1 2.083 9H3.1z"></path>
                                </svg>
                            </button>
                            <button type="button" class="btn btn-primary" value="Delete"
                                    id="delete-booking-button">
                                <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-trash3-fill" viewBox="0 0 16 16">
                                    <path d="M11 1.5v1h3.5a.5.5 0 0 1 0 1h-.538l-.853 10.66A2 2 0 0 1 11.115 16h-6.23a2 2 0 0 1-1.994-1.84L2.038 3.5H1.5a.5.5 0 0 1 0-1H5v-1A1.5 1.5 0 0 1 6.5 0h3A1.5 1.5 0 0 1 11 1.5Zm-5 0v1h4v-1a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5ZM4.5 5.029l.5 8.5a.5.5 0 1 0 .998-.06l-.5-8.5a.5.5 0 1 0-.998.06Zm6.53-.528a.5.5 0 0 0-.528.47l-.5 8.5a.5.5 0 0 0 .998.058l.5-8.5a.5.5 0 0 0-.47-.528ZM8 4.5a.5.5 0 0 0-.5.5v8.5a.5.5 0 0 0 1 0V5a.5.5 0 0 0-.5-.5Z"></path>
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
                <hr>

                <!-- BOOKINGS TABLE -->
                <div class="row mt-5">
                    <div class="table-responsive text-nowrap table-container-size" id="table-container">
                        <table class="table table-striped" id="booking-table">
                            <thead>
                            <tr>
                                <th class="no-print" scope="col"></th>
                                <th scope="col">ID</th>
                                <th scope="col">CHECK-IN</th>
                                <th scope="col">CHECK-OUT</th>
                                <th scope="col">Room</th>
                                <th scope="col">Balance</th>
                                <th scope="col">Guest ID</th>
                                <th scope="col">User ID</th>
                            </tr>
                            </thead>
                            <tbody id="tbody-booking"></tbody>
                        </table>
                    </div>
                </div>

            </form>

            <!-- CREATE BOOKING MODAL -->
            <div id="add-booking-modal" class="modal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form id="create-booking-form" method="POST"
                              action="${pageContext.request.contextPath}/rest/booking/insert-one">

                            <!-- MODAL HEADING -->
                            <div class="modal-header">
                                <h5 class="modal-title">New Booking</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>

                            <!-- MODAL BODY -->
                            <div class="modal-body">

                                <div class="row mb-3">
                                    <label for="check_in" class="col-sm-2 col-form-label">Check-in:</label>
                                    <div class="col-sm-10">
                                        <input required class="form-control" type="datetime-local" id="check_in"
                                               name="check_in"/>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="check_out" class="col-sm-2 col-form-label">Check-out:</label>
                                    <div class="col-sm-10">
                                        <input required class="form-control" type="datetime-local" id="check_out"
                                               name="check_out"/>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="add-booking-select-room" class="col-sm-2 col-form-label">Room:</label>
                                    <div class="col-sm-10">
                                        <select required id="add-booking-select-room" class="form-select"
                                                aria-label="Select the room" size="4"
                                                name="room">
                                            <option value="A1">A1</option>
                                            <option value="A2">A2</option>
                                            <option value="A3">A3</option>
                                            <option value="A4">A4</option>
                                            <option value="A5">A5</option>
                                            <option value="A6">A6</option>
                                            <option value="A7">A7</option>
                                            <option value="A8">A8</option>
                                            <option value="A9">A9</option>
                                            <option value="A10">A10</option>
                                        </select>
                                    </div>
                                </div>
                                <input required class="form-control" type="hidden" id="balance" name="balance"
                                       value="0"/>
                                <div class="row mb-3">
                                    <label for="add-booking-select-guest" class="col-sm-2 col-form-label">Guest
                                        ID:</label>
                                    <div class="col-sm-10">
                                        <select required id="add-booking-select-guest" class="form-select"
                                                name="guest_id">
                                            <option selected disabled value="">Guest Name</option>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" id="user_id" name="user_id"
                                       value="<c:out value="${sessionScope.user_id}"/>">

                                <!-- MODAL FOOTER -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel
                                    </button>
                                    <button type="submit" class="btn btn-primary">Create</button>
                                </div>

                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- CREATE MATCH MODAL -->
            <div id="add-match-modal" class="modal" tabindex="0">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form id="create-match-form" method="POST"
                              action="${pageContext.request.contextPath}/rest/match/insert-one">

                            <!-- MODAL HEADING -->
                            <div class="modal-header">
                                <h5 class="modal-title">Match Manager</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>

                            <!-- MODAL BODY -->
                            <div class="modal-body">

                                <!-- MODAL BODY INPUTS -->
                                <div class="row mb-3">
                                    <label for="match-modal-text-booking-id" class="col-sm-2 col-form-label">Booking ID:</label>
                                    <div class="col-sm-10">
                                        <input required type="text" class="form-control" id="match-modal-text-booking-id" name="booking_id" readonly>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="match-modal-select-rfid-code" class="col-sm-2 col-form-label">RFID code:</label>
                                    <div class="col-sm-10">
                                        <select required id="match-modal-select-rfid-code" class="form-select"
                                                name="code_rfid" size="4">
                                            <option selected value="">No filter</option>
                                        </select>
                                    </div>
                                </div>
                                <hr/>
                                <div class="row mt-5">
                                    <div class="table-responsive text-nowrap">
                                        <table class="table table-striped" id="match-table">
                                            <thead>
                                            <tr>
                                                <th scope="col">ID</th>
                                                <th scope="col">BOOKING ID</th>
                                                <th scope="col">RFID CODE</th>
                                                <th scope="col">DATE & TIME</th>
                                                <th scope="col">STATUS</th>
                                            </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                    </div>
                                </div>
                                <input type="hidden" name="status"
                                       value="true"/>
                                <!-- MODAL FOOTER -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel
                                    </button>
                                    <button type="submit" class="btn btn-primary">Create</button>
                                </div>
                            </div>
                        </form>

                    </div>
                </div>
            </div>

        </div>

        <p id="create-booking-form-output"></p>
        <div data-autoload="templates/footer.html footer">
            <!-- FOOTER -->
        </div>
        <script src="js/jquery-3.6.0.min.js"></script>
        <script src="js/jquery.serializejson.min.js"></script>
        <script src="js/contextPath.js"></script>
        <script src="bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="js/autoloadLib.js"></script>
        <script src="js/common.js"></script>
        <script src="js/booking-match-manager.js"></script>

        </body>

        </html>
    </c:otherwise>
</c:choose>
