<%--suppress ALL --%>
<!--
Copyright 2018 University of Padua, Italy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Author: Enrico Sgarbossa (enrico.sgarbossa.1@studenti.unipd.it)
Version: 1.0
Since: 1.0
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<c:choose>
    <c:when test="${empty sessionScope.user_id || sessionScope.user_permission_level.toLowerCase() != 'admin'}">
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
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
            <link rel="icon" type="image/x-icon" href="images/favicon.ico">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.2/font/bootstrap-icons.css">
            <link href="css/common.css" rel="stylesheet">
            <title>Countability</title>
        </head>
        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-countability">
            <!-- NAV -->
        </div>
        <div class="container">
            <div class="row mt-5 align-items-center">
                <div class="col-10">
                    <h1 class="display-4 text-center">Transaction list</h1>
                </div>
                <div class="col-md-2">
                    <button type="button" class="btn btn-secondary btn-sm" onclick="window.print()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-printer-fill" viewBox="0 0 16 16">
                            <path d="M5 1a2 2 0 0 0-2 2v1h10V3a2 2 0 0 0-2-2H5zm6 8H5a1 1 0 0 0-1 1v3a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-3a1 1 0 0 0-1-1z"/>
                            <path d="M0 7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2h-1v-2a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v2H2a2 2 0 0 1-2-2V7zm2.5 1a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1z"/>
                        </svg>
                    </button>
                </div>
            </div>

            <hr/>
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
                            <option value="false">Nullified</option>
                            <option value="true">Successful</option>
                        </select>
                    </div>
                    <div class="col-md-2 mt-2 mt-md-0">
                        <button type="submit" class="btn btn-primary">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor"
                                 class="bi bi-funnel-fill"
                                 viewBox="0 0 16 16">
                                <path d="M1.5 1.5A.5.5 0 0 1 2 1h12a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.128.334L10 8.692V13.5a.5.5 0 0 1-.342.474l-3 1A.5.5 0 0 1 6 14.5V8.692L1.628 3.834A.5.5 0 0 1 1.5 3.5v-2z"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </form>
            <hr/>
            <form id="form-action-on-selected">
                <div class="row mb-4 text-center align-items-center no-print">
                    <div class="col-sm">
                        With selected:
                    </div>
                    <div class="col-sm">
                        <select required id="select-action" class="form-select form-select-sm"
                                aria-label="selected action">
                            <option selected disabled value="">Select action</option>
                            <option value="delete">Nullify</option>
                        </select>
                    </div>
                    <div class="col-sm mt-2 mt-md-0">
                        <button class="btn btn-sm btn-outline-danger" type="submit">Execute</button>
                    </div>
                </div>
                <div class="row mt-5">
                    <div class="table-responsive text-nowrap table-container-size">
                        <table class="table table-striped" id="table-transactions">
                            <thead>
                            <tr>
                                <th class="no-print" scope="col"></th>
                                <th scope="col">ID</th>
                                <th scope="col">Date</th>
                                <th scope="col">Booking ID</th>
                                <th scope="col">Type</th>
                                <th scope="col">Amount</th>
                                <th scope="col">State</th>
                                <th scope="col">Description</th>
                                <th scope="col">User ID</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th scope="row" colspan="9" class="text-center">No data</th>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
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
        <script src="js/countability.js"></script>
        </body>
        </html>
    </c:otherwise>
</c:choose>
