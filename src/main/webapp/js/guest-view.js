let guestId;
let outputElements = {};
let tBody;
let trTemplate = $("<tr>" +
    "<th scope='row' class='td-id'></th>" +
    "<td class='td-type'></td>" +
    "<td class='td-state'></td>" +
    "<td class='td-amount'></td>" +
    "<td class='td-description'></td>" +
    "<td class='td-date'></td>" +
    "<td class='td-user'></td>" +
    "</tr>");

let filterDateStart;
let filterDateEnd;
let filterState;
let filterType;

function getTableData(callback) {
    $.ajax({
        url: contextPath + "/rest/transactions/all-guest-booking-transactions?guest_id=" + guestId,
        type: 'GET',
        success: callback,
        error: function (data) {
            console.log('An error occurred.');
            console.log(data);
        }
    });
}

function refreshTable() {
    getTableData((result) => {
        if (result.status) {
            tBody.empty();
            let dateStart;
            let dateEnd;
            if (filterDateStart) {
                dateStart = new Date(filterDateStart);
                dateStart.setHours(0);
                dateStart.setMinutes(0);
                dateStart.setSeconds(0);
                dateStart.setMilliseconds(0);
            }
            if (filterDateEnd) {
                dateEnd = new Date(filterDateEnd);
                dateEnd.setHours(23);
                dateEnd.setMinutes(59);
                dateEnd.setSeconds(59);
                dateEnd.setMilliseconds(999);
            }
            for (let obj of result.data) {

                if (filterType && filterType.toUpperCase() !== obj.type.toUpperCase()) {
                    continue;
                }
                if (filterState && filterState !== obj.state.toString()) {
                    continue;
                }

                if (filterDateStart) {
                    let objectDate = new Date(obj.datetime);
                    if (dateStart > objectDate) {
                        continue;
                    }
                }

                if (filterDateEnd) {
                    let objectDate = new Date(obj.datetime);
                    if (dateEnd < objectDate) {
                        continue;
                    }
                }

                let tr = trTemplate.clone();
                tr.find(".td-id").html(obj.id);
                tr.find(".td-type").html(obj.type);
                tr.find(".td-state").html(obj.state.toString());
                tr.find(".td-amount").html(obj.amount);
                tr.find(".td-description").html(obj.description);
                tr.find(".td-date").html(obj.datetime);
                tr.find(".td-user").html(obj.user_id);
                tBody.append(tr);
            }
        } else {
            console.log(result);
        }
    });
}

function getGuestName(callback) {
    $.ajax({
        url: contextPath + "/rest/guest/" + guestId,
        type: 'GET',
        success: callback,
        error: function (data) {
            console.log('An error occurred.');
            console.log(data);
        }
    });
}

function getRoomNumberAndBalance(callback) {
    $.ajax({
        url: contextPath + "/rest/booking/last-booking/" + guestId,
        type: 'GET',
        success: callback,
        error: function (data) {
            console.log('An error occurred.');
            console.log(data);
        }
    });
}

function refreshOutputs() {
    getGuestName((result) => {
        if (result.status) {
            if (outputElements["guest-name"]) {
                for (let jElement of outputElements["guest-name"]) {
                    jElement.html(result?.data?.name + " " + result?.data?.surname);
                }
            }
        }
    });

    getRoomNumberAndBalance((result) => {
        if (result.status) {
            if (outputElements["room-number"]) {
                for (let jElement of outputElements["room-number"]) {
                    jElement.html(result?.data?.room);
                }
            }
            if (outputElements["balance"]) {
                for (let jElement of outputElements["balance"]) {
                    jElement.html(result?.data?.balance + "$");
                }
            }
        }
    });
}

function setFilters() {
    filterDateStart = $("#filter-date-start").val();
    filterDateEnd = $("#filter-date-end").val();
    filterState = $("#select-filter-state").val();
    filterType = $("#select-filter-type").val();
}

$(() => {

    guestId = $("#guest-id").html();
    tBody = $($("#table-details").find("tbody"));

    $("*").each(function (index, element) {
        let jElement = $(element);
        if (jElement.hasClass("output-guest-name")) {
            if (!outputElements["guest-name"]) {
                outputElements["guest-name"] = [];
            }
            outputElements["guest-name"].push(jElement);
        }
        if (jElement.hasClass("output-room-number")) {
            if (!outputElements["room-number"]) {
                outputElements["room-number"] = [];
            }
            outputElements["room-number"].push(jElement);
        }

        if (jElement.hasClass("output-balance")) {
            if (!outputElements["balance"]) {
                outputElements["balance"] = [];
            }
            outputElements["balance"].push(jElement);
        }
    });

    $("#form-filter").on("submit", (event) => {
        event.preventDefault();
        setFilters();
        if (filterDateStart && filterDateEnd && filterDateEnd < filterDateStart) {
            alert("Error!\nDate's filters are not correct.\nRemember that 'End Date' must be equal or bigger than 'Start Date'.\nCheck them and retry.");
            return;
        }
        refreshTable();
    });

    refreshOutputs();
    refreshTable();
});
