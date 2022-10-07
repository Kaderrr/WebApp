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
    <c:when test="${empty sessionScope.user_id}">
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
            <title>Services Area</title>
        </head>
        <body class="d-flex flex-column h-100">
        <a style="display: none" id="context-path">${pageContext.request.contextPath}</a>
        <div data-autoload="templates/nav.html nav" data-setactive="#nav-services">
            <!-- NAV -->
        </div>
        <div class="container">
            <div class="row my-5">
                <h1 class="display-4 text-center">Create Transaction</h1>
            </div>
            <hr/>
            <form id="form-add-transaction" method="POST"
                  action="${pageContext.request.contextPath}/rest/transactions/insert-one">
                <div class="row align-items-end">
                    <div class="col-md text-center text-lg-end">
                        <input type="radio" class="form-check-input" id="add-transaction-payment" name="type"
                               value="payment" checked/>
                        <label class="form-check-label" for="add-transaction-payment">Payment</label>
                        <c:if test='${sessionScope.user_permission_level.toLowerCase() != "service_provider"}'>
                            <br/>
                            <input type="radio" class="form-check-input" id="add-transaction-deposit" name="type"
                                   value="deposit">
                            <label class="form-check-label" for="add-transaction-deposit">Deposit</label>
                        </c:if>
                    </div>
                    <div class="col-md">
                        <label for="add-transaction-amount">Amount:</label>
                        <div class="input-group">
                            <input required type="number" step="0.1" id="add-transaction-amount" class="form-control"
                                   name="amount"/>
                            <div class="input-group-text">€</div>
                        </div>
                    </div>
                    <!-- <label for="userId">User:</label>
                    <input type="text" id="userId" name="userId" value=""><br/> readonly -->
                    <input type="hidden" id="add-transaction-user-id" name="user_id"
                           value="<c:out value="${sessionScope.user_id}"/>">
                    <input type='hidden' name='booking_id' id="add-transaction-booking-id" value=''/>
                    <input type="hidden" name="state" value="true"/>
                    <div class="col-md">
                        <label for="add-transaction-code-rfid">RFID code:</label>
                        <input required type="text" step="1" min="1" id="add-transaction-code-rfid"
                               class="form-control">
                    </div>
                    <div class="col-md mt-3 mt-md-0 text-lg-start text-end">
                        <button type="submit" class="col-3 btn btn-primary" id="add-transaction-submit"><i
                                class="bi bi-send"></i></button>
                        <button type="reset" class="btn btn-outline-warning" id="add-transaction-reset"><i
                                class="bi bi-arrow-repeat"></i></button>
                    </div>
                </div>
            </form>
            <br/>
            <div class="row mt-3 text-center">
                <div id="add-transaction-result" class="p-3 mb-2 col-md text-white"></div>
            </div>

            <table id="table-transaction" style="display:none;" border="1">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Amount</th>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Booking_id</th>
                    <th>State</th>
                    <th>Type</th>
                    <th>User_id</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <hr/>
            <div class="row mt-3 align-items-end text-end">
                <form id="form-delete-last-transaction">
                    <!-- method="PUT" action="${pageContext.request.contextPath}/rest/transactions/delete-last-by-user-id"> -->
                    <input type="hidden" id="user-id" class="form-control"
                           name="user_id" value="${sessionScope.user_id}">
                    <button type="submit" class="btn btn-danger">Nullify last</button>
                </form>
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
        <script src="js/services.js"></script>
        </body>
        </html>
    </c:otherwise>
</c:choose>
