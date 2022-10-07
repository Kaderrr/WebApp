let trTemplate = $("<tr>" +
    "<td class=no-print'><div class='form-check'><input class='form-check-input' type='checkbox' id='selectedTransactions' name='selectedTransactions'></div></td>" +
    "<th class='td-id'></th>" +
    "<td class='td-date'></td>" +
    "<td class='td-booking'></td>" +
    "<td class='td-type'></td>" +
    "<td class='td-amount'></td>" +
    "<td class='td-state'></td>" +
    "<td class='td-description'></td>" +
    "<td class='td-user'></td>" +
    "</tr>");
let filterDateStart;
let filterDateEnd;
let filterState;
let filterType;
let totalAmount = 0;
let totalPayments = 0;
let totalDeposits = 0;

function getAllTransactions(callback) {
    let url = contextPath + "/rest/transactions/all";
    request = $.ajax({
        url: url, method: "GET", contentType: "application/json", dataType: "json"
    });
    request.done(callback);
    request.fail(function () {
        console.log("Failed request to " + url);
    });
}

function getAllTransactionsBetweenDates(callback) {
    let url = contextPath + "/rest/transactions/between-dates";
    let fromDate = filterDateStart//+ " 00:00:00.000";
    let toDate = filterDateEnd//+ " 23:59:59.999";
    let fromDateConverted = new Date(fromDate);
    let toDateConverted = new Date(toDate);
    fromDateConverted.setMonth(fromDateConverted.getMonth() + 1);
    toDateConverted.setMonth(toDateConverted.getMonth() + 1);
    //let fromDateStr = fromDateConverted.getFullYear()+"-"+fromDateConverted.getMonth()+"-"+fromDateConverted.getDate()+" "+fromDateConverted.getHours()+":"+fromDateConverted.getMinutes()+":"+fromDateConverted.getSeconds();
    let fromDateStr = fromDateConverted.getFullYear() + "-" + fromDateConverted.getMonth() + "-" + fromDateConverted.getDate() + " 00:00:00.000";
    let toDateStr = toDateConverted.getFullYear() + "-" + toDateConverted.getMonth() + "-" + toDateConverted.getDate() + " " + "23" + ":" + "59" + ":" + "59";
    request = $.ajax({
        url: url,
        data: {
            "fromDate": fromDateStr, "toDate": toDateStr
        },
        type: "POST", contentType: "application/x-www-form-urlencoded"
    });
    request.done(callback);
    request.fail(function () {
        console.log("Failed request to " + url);
    });
}

function printTransactionsInTable(jTbody, transactions) {
    jTbody.empty();
    totalDeposits = 0;
    totalPayments = 0;
    totalAmount = 0;
    for (let transaction of transactions) {

        if (filterType && filterType.toUpperCase() !== transaction.type.toUpperCase()) {
            continue;
        }
        if (filterState && filterState.toUpperCase() !== transaction.state.toString().toUpperCase()) {
            continue;
        }

        /*if (filterDateStart) {
            let dateStart = new Date(filterDateStart);
            let transactionDate = new Date(transaction.datetime);
            if (dateStart >= transactionDate) {
                continue;
            }
        }

        if (filterDateEnd) {
            let dateEnd = new Date(filterDateEnd);
            let transactionDate = new Date(transaction.datetime);
            if (dateEnd < transactionDate) {
                continue;
            }
        }*/
        let tr = trTemplate.clone();
        let input = tr.find("input");
        input.attr("value", transaction.id);
        tr.find(".td-id").html(transaction.id);
        tr.find(".td-type").html(transaction.type);
        tr.find(".td-state").html(transaction.state.toString());
        tr.find(".td-amount").html(transaction.amount);
        tr.find(".td-description").html(transaction.description);
        tr.find(".td-date").html(transaction.datetime);
        tr.find(".td-user").html(transaction.user_id);
        tr.find(".td-booking").html(transaction.booking_id);
        jTbody.append(tr);
        if (transaction.state && transaction.type.toUpperCase() === "DEPOSIT") {
            totalDeposits += transaction.amount;
            totalAmount += transaction.amount;
        } else if (transaction.state && transaction.type.toUpperCase() === "PAYMENT") {
            totalPayments += transaction.amount;
            totalAmount -= transaction.amount;
        }
    }
}

function refreshTransactionsTable() {
    let tableBody = $($("#table-transactions").find("tbody"));
    if (filterDateStart && filterDateEnd && filterDateStart <= filterDateEnd) {
        getAllTransactionsBetweenDates((jsonData) => {
            if (jsonData.status) {
                printTransactionsInTable(tableBody, jsonData?.data);
            } else {
                console.log(jsonData);
            }
        });
    } else {
        getAllTransactions((jsonData) => {
            if (jsonData?.status) {
                printTransactionsInTable(tableBody, jsonData?.data);
            } else {
                console.log(jsonData);
            }
        });
    }
}

function actionOnSelected_delete() {
    let idsArray = $("#form-action-on-selected input:checkbox:checked").map(function () {
        return $(this).val();
    }).get();
    for (let id of idsArray) {
        $.ajax({
            url: contextPath + "/rest/transactions/delete-one",
            data: $.param({id}),
            method: "DELETE",
            async: false,
            contentType: "application/x-www-form-urlencoded",
            dataType: "json"
        }).done((response) => {
            if (response.status) {
                refreshTransactionsTable();
            } else {

            }
            console.log(response);
        });
    }
}

function setFilters() {
    filterDateStart = $("#filter-date-start").val();
    filterDateEnd = $("#filter-date-end").val();
    filterState = $("#select-filter-state").val();
    filterType = $("#select-filter-type").val();
}

$(function () {
    let jFormActionSelected = $("#form-action-on-selected");
    jFormActionSelected.on("submit", function (event) {
        event.preventDefault();
        let action = $("#select-action").val();
        switch (action) {
            case "delete":
                actionOnSelected_delete(jFormActionSelected);
                break;
        }
    });

    $("#form-filter").on("submit", (e) => {
        e.preventDefault();
        setFilters();
        if (filterDateStart && filterDateEnd && filterDateEnd < filterDateStart) {
            alert("Error!\nDate's filters are not correct.\nRemember that 'End Date' must be equal or bigger than 'Start Date'.\nCheck them and retry.");
            return;
        }
        refreshTransactionsTable();
    });
    refreshTransactionsTable();
})
