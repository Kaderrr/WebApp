let removeClass = "bg-success bg-danger bg-warning";
let addClass = "";
let divResult = $("#add-transaction-result");

function writeOnSpan(data, div, removeClass, addClass) {
    div.html(data);
    div.removeClass(removeClass);
    div.addClass(addClass);
}

function getBookingIDFromRfid(code_rfid, callback) {
    request = $.ajax({
        url: contextPath + "/rest/match/last-booking-of-rfid/" + code_rfid,
        method: "GET",
        contentType: "application/json",
        dataType: "json"
    }).done((response) => {
        if (response.status) {
            callback(response.data.booking_id);
        } else {
            writeOnSpan("Unable to find booking associated to rfid: " + code_rfid + ".", divResult, removeClass, "bg-danger");
            let booking_id = -1;
            callback(booking_id);
        }
    });
}

function addTransactionResultHandler(data) {
    if (data.status) {
        addClass = "bg-success";
        writeOnSpan("Added " + data?.data?.type + ": " + data?.data?.amount + "€ to booking " + data?.data?.booking_id, divResult, removeClass, addClass);
    } else {
        alert(data.statusMessage);
        addClass = "bg-danger";
        writeOnSpan(data.statusMessage, divResult, removeClass, addClass);
    }
}

function deleteLast() {
    let user_id = $("#user-id").val();
    $.ajax({
        url: contextPath + "/rest/transactions/delete-last-by-user-id",
        data: $.param({user_id}),
        method: "DELETE",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json"
    }).done((response) => {
        if (response.status) {
            writeOnSpan("Deleted " + response?.data?.type + ": " + response?.data?.amount + "€ from booking " + response?.data?.booking_id, divResult, removeClass, "bg-warning");
        } else {
            writeOnSpan(response.statusMessage, divResult, removeClass, "bg-danger");
        }
    });
}

$(function () {
    let jFormInsertOne = $("#form-add-transaction");

    jFormInsertOne.on("submit", function (event) {
        divResult.visibility = false;
        divResult.removeClass("bg-success bg-danger bg-warning");
        event.preventDefault();
        let code_rfid = $("#add-transaction-code-rfid").val();
        getBookingIDFromRfid(code_rfid, (booking_id) => {
            if (booking_id < 0) {
                return;
            }
            $("#add-transaction-booking-id").val(booking_id);
            createFormAjaxRequest(event.target, addTransactionResultHandler);
        });
    });

    jFormInsertOne.on("reset", function () {
        divResult.visibility = false;
        divResult.removeClass("bg-success bg-danger");
    });

    let jFormDeleteLast = $("#form-delete-last-transaction");
    jFormDeleteLast.on("submit", function (event) {
        divResult.visibility = false;
        divResult.removeClass("bg-success bg-danger bg-warning");
        event.preventDefault();
        //createFormAjaxRequest(event.target, deleteLastTransactionResultHandler)
        deleteLast();
    });
})
