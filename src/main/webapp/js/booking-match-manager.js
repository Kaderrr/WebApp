let trTemplateBooking = $("<tr>" +
    "<td class='no-print'>" +
    "<div class='form-check'>" +
    "<input class='form-check-input check-booking' type='checkbox' id='selectedBookings' name='selectedBookings'>" +
    "</div>" +
    "</td>" +
    "<th class='td-id' scope='row'></th>" +
    "<td class='td-checkin'></td>" +
    "<td class='td-checkout'></td>" +
    "<td class='td-room'></td>" +
    "<td class='td-balance'></td>" +
    "<td class='td-guest'></td>" +
    "<td class='td-user'></td>" +
    "</tr>");

let trTemplateMatch = $("<tr>" +
    "<th class='td-id' scope='row'></th>" +
    "<td class='td-booking-id'></td>" +
    "<td class='td-code-rfid'></td>" +
    "<td class='td-datetime'></td>" +
    "<td class='td-status'></td>" +
    "</tr>");

let bookingTableID = $("#booking-table");
let bookingTableTbodyID = $("#tbody-booking");
let matchTableID = $("#match-table");
let selectCheckin = $("#check_in");
let selectCheckout = $("#check_out");
let selectRoom = $("#add-booking-select-room");
let selectGuest = $("#add-booking-select-guest");
let selectCheckinFilter = $("#filter-checkin");
let selectCheckoutFilter = $("#filter-checkout");
let selectGuestFilter = $("#select-filter-guest");
let selectRoomFilter = $("#select-filter-room");
let manageMatchButton = $("#btn-add-match");
let deleteBookingButton = $("#delete-booking-button");
let resetFilterButton = $("#reset-filter-button");
let textMatchBookingID = $("#match-modal-text-booking-id");
let selectMatchRfidCodes = $("#match-modal-select-rfid-code");
let formCreateBooking = $("#create-booking-form");
let formCreateMatch = $("#create-match-form");
let formFilterBooking = $("#filter-delete-booking-form");

let filterCheckin;
let filterCheckout;
let filterGuest;
let filterRoom;

// GET AND PRINT BOOKINGS
function getBookings(callback) {
    let request = $.ajax({
        url: contextPath + "/rest/booking/all",
        method: "GET",
        contentType: "application/json"
    });
    request.done(result => {
        if (!result.status) {
            console.log(result);
            return;
        }
        callback(result.data);
    });
}
function getBookingsBetweenDates(callback) {
    let url = contextPath + "/rest/booking/between-dates";
    let fromDate = filterCheckin; //+ " 00:00:00.000";
    let toDate = filterCheckout; //+ " 23:59:59.999";
    let fromDateConverted = new Date(fromDate);
    let toDateConverted = new Date(toDate);
    fromDateConverted.setMonth(fromDateConverted.getMonth() + 1);
    toDateConverted.setMonth(toDateConverted.getMonth() + 1);
    let fromDateStr = fromDateConverted.getFullYear() + "-" + fromDateConverted.getMonth() + "-" + fromDateConverted.getDate() + " 00:00:00.000";
    let toDateStr = toDateConverted.getFullYear() + "-" + toDateConverted.getMonth() + "-" + toDateConverted.getDate() + " " + "23" + ":" + "59" + ":" + "59";
    request = $.ajax({
        url: url,
        data: {
            "fromDate": fromDateStr, "toDate": toDateStr
        },
        type: "POST", contentType: "application/x-www-form-urlencoded"
    });
    request.done(result => {
        if (!result.status) {
            console.log(result);
            return;
        }
        callback(result.data);
    });
    request.fail(function () {
        console.log("Failed request to " + url);
    });
}
function printAllBookings(data, table) {
    let jTableElement = $(table);
    let jTbody = jTableElement.find("tbody");
    jTbody.empty();
    for (let item of data) {

        if (filterGuest && filterGuest.toUpperCase() !== item.guest_id.toUpperCase()) {
            continue;
        }
        if (filterRoom && filterRoom.toUpperCase() !== item.room.toUpperCase()) {
            continue;
        }

        let tr = trTemplateBooking.clone();
        let input = tr.find("input");
        input.attr("value", item.id);
        tr.find(".td-id").html(item.id);
        tr.find(".td-checkin").html(item.check_in);
        tr.find(".td-checkout").html(item.check_out);
        tr.find(".td-room").html(item.room);
        tr.find(".td-balance").html(item.balance);
        tr.find(".td-guest").html(item.guest_id);
        tr.find(".td-user").html(item.user_id);
        jTbody.append(tr);
    }
}
function printAllBookingIDOnSelect(data, select) {
    for (let item of data) {
        select.append($('<option>', {
            value: item.id,
            text: item.id
        }));
    }
}

// GET AND PRINT GUESTS
function getAllGuests(callback) {
    let request = $.ajax({
        url: contextPath + "/rest/guest/all-ordered-by-surname",
        method: "GET",
        contentType: "application/json"
    });
    request.done(result => {
        if (!result.status) {
            console.log(result);
            return;
        }
        callback(result.data);
    });
}
function printAllGuests(data, select) {
    for (let item of data) {
        select.append($('<option>', {
            value: item.id,
            text: item.surname + " " + item.name
        }));
    }
}

// GET AND PRINT MATCHES
function getAllMatchesOfBooking(callback) {
    let booking_id = textMatchBookingID.val();
    let request = $.ajax({
        url: contextPath + "/rest/match/all-of-booking/" + booking_id,
        method: "GET",
        contentType: "application/json"
    });
    request.done(result => {
        if (!result.status) {
            console.log(result);
            return;
        }
        callback(result.data);
    });
}
function printAllMatchesOnTable(matches, table) {
    let jTableElement = $(table);
    let jTbody = jTableElement.find("tbody");
    jTbody.empty();
    for (let item of matches) {

        let tr = trTemplateMatch.clone();
        tr.find(".td-id").html(item.id);
        tr.find(".td-booking-id").html(item.booking_id);
        tr.find(".td-code-rfid").html(item.code_rfid);
        tr.find(".td-datetime").html(item.datetime);
        tr.find(".td-status").html(item.status.toString());
        jTbody.append(tr);
    }
}

// GET AND PRINT RFID
function getAllUnusedRfid(callback) {
    let request = $.ajax({
        url: contextPath + "/rest/rfid/all-unused",
        method: "GET",
        contentType: "application/json"
    });
    request.done(result => {
        if (!result.status) {
            console.log(result);
            return;
        }
        callback(result.data);
    });
}
function printRfidCodes(data, select) {
    select.empty();
    for (let item of data) {
        select.append($('<option>', {
            value: item.code,
            text: item.code
        }));
    }
}

// FUNCTION TO CREATE NEW BOOKINGS AND NEW MATCHES
function createBookingRequest(event, bookingTable) {
    let formObject = $(event.target);
    let jsonData = formObject.serializeJSON();
    let myDate = new Date(jsonData.check_in);
    myDate.setMonth(myDate.getMonth() + 1);
    let str = myDate.getFullYear() + "-" + myDate.getMonth() + "-" + myDate.getDate() + " " + myDate.getHours() + ":" + myDate.getMinutes() + ":" + myDate.getSeconds();
    jsonData.check_in = str;
    myDate = new Date(jsonData.check_out);
    myDate.setMonth(myDate.getMonth() + 1);
    str = myDate.getFullYear() + "-" + myDate.getMonth() + "-" + myDate.getDate() + " " + myDate.getHours() + ":" + myDate.getMinutes() + ":" + myDate.getSeconds();
    jsonData.check_out = str;

    let request = $.ajax({
        url: formObject.attr("action"),
        method: formObject.attr("method"),
        data: JSON.stringify(jsonData),
        dataType: "json",
        contentType: "application/json"
    });
    request.done((result) => {
        if (!result.status) {
            console.log(result);
            return;
        }
        getBookings((data) => {
            printAllBookings(data, bookingTable);
            printAllBookingIDOnSelect(data, selectMatchRfidCodes)
        });
        getAllUnusedRfid((data) => {
            printRfidCodes(data, selectMatchRfidCodes);
        });
    });
}
function createMatchRequest(event, matchTable) {
    let formObject = $(event.target);
    let jsonData = formObject.serializeJSON();
    let request = $.ajax({
        url: formObject.attr("action"),
        method: formObject.attr("method"),
        data: JSON.stringify(jsonData),
        dataType: "json",
        contentType: "application/json"
    });
    request.done((result) => {
        if (!result.status) {
            console.log(result);
            return;
        }
        getAllMatchesOfBooking((data) => {
            printAllMatchesOnTable(data, matchTable);
        });
    });
    request.fail(function () {
        alert("Request failed.\nCheck if data are setted properly and retry.");
    });
}

// FUNCTION TO DELETE BOOKINGS
function deleteSelected() {
    let idsArray = getCheckedBookings();
    for (let id of idsArray) {
        $.ajax({
            url: contextPath + "/rest/booking/delete/" + id, method: "delete", dataType: "json"
        }).done(() => {
            refreshBookingTable();
        });
    }
}
function getCheckedBookings() {
    return $("#filter-delete-booking-form input:checkbox:checked").map(function () {
        return $(this).val();
    }).get();
}

// FUNCTION TO MANAGE THE PAGE
function setFilters() {
    filterCheckin = selectCheckinFilter.val();
    filterCheckout = selectCheckoutFilter.val();
    filterGuest = selectGuestFilter.val();
    filterRoom = selectRoomFilter.val();
}
function refreshBookingTable() {
    getBookings((data) => {
        printAllBookings(data, bookingTableID);
    });
}
function closeModal(modalID) {
    let myModal = bootstrap.Modal.getInstance(modalID);
    myModal.hide();
}
function resetFormBooking() {
    selectCheckin.val("");
    selectCheckout.val("");
    selectRoom.val("");
    selectGuest.val("").change();
}
function resetFilter() {
    selectCheckinFilter.val("");
    selectCheckoutFilter.val("");
    selectGuestFilter.val("");
    selectRoomFilter.val("");
}

$(document).ready(() => {

    // INITIAL SETTINGS
    manageMatchButton.prop('disabled', true);
    textMatchBookingID.prop('readonly', true);

    // INITIALIZATION
    getBookings((data) => {
        printAllBookings(data, bookingTableID);
    });
    getAllGuests((data) => {
        printAllGuests(data, selectGuest);
        printAllGuests(data, selectGuestFilter);
    });
    getBookings((data) => {
        printAllBookingIDOnSelect(data, textMatchBookingID);
    });

    // BOOKING FUNCTIONS
    formCreateBooking.submit((e) => {
        e.preventDefault();
        createBookingRequest(e, bookingTableID);
        closeModal($("#add-booking-modal"));
        resetFormBooking();
    });
    deleteBookingButton.on("click", (e) => {
        e.preventDefault();
        deleteSelected();
        manageMatchButton.prop('disabled', true);
    });

    // FILTER FUNCTIONS
    formFilterBooking.on("submit", (e) => {
        e.preventDefault();
        setFilters();
        if (filterCheckout && filterCheckin && filterCheckout < filterCheckin) {
            alert("Error!\nDate's filters are not correct.\nRemember that 'Check-out' must be equal or bigger than 'Check-in'.\nCheck them and retry.");
            return;
        }
        if (filterCheckout && filterCheckin && filterCheckout >= filterCheckin) {
            getBookingsBetweenDates((data) => {
                printAllBookings(data, bookingTableID);
            });
        } else {
            getBookings((data) => {
                printAllBookings(data, bookingTableID);
            });
            if (filterCheckout || filterCheckin) {
                alert("Attention!\nYou can not set only one date filter.\nAll bookings will be displayed.")
            }
        }
    });
    resetFilterButton.on("click", (e) => {
        e.preventDefault();
        resetFilter();
        getBookings((data) => {
            printAllBookings(data, bookingTableID);
        });
    });

    // MATCH FUNCTIONS
    manageMatchButton.on("click", (e) => {
        e.preventDefault();
        getAllMatchesOfBooking((data) => {
            printAllMatchesOnTable(data, matchTableID);
        });
        getAllUnusedRfid((data) => {
            printRfidCodes(data, selectMatchRfidCodes);
        });
    });
    formCreateMatch.submit((e) => {
        e.preventDefault();
        createMatchRequest(e, matchTableID);
        closeModal($("#add-match-modal"));
    });
    bookingTableTbodyID.on('change', '.check-booking', function () {
        let idsArray = getCheckedBookings();
        if (idsArray.length === 1) {
            manageMatchButton.prop('disabled', false);
            textMatchBookingID.val(idsArray[0]);
        } else {
            manageMatchButton.prop('disabled', true);
        }
    });
});

