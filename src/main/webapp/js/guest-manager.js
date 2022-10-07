let trTemplate = $("<tr>                    <td class=\"no-print\">                        <div class=\"form-check\">                            <input class=\"form-check-input\" type=\"checkbox\" name=\"selectedGuests\">                        </div>                    </td>                    <th class=\"td-id\" scope=\"row\"></th>                    <td class=\"td-name\"></td>                    <td class=\"td-surname\"></td>                    <td class=\"td-type\"></td>                </tr>");

let filterType;
let filterName;
let filterSurname;

function getAllGuests(callback) {
    let url = contextPath + "/rest/guest/all";
    request = $.ajax({
        url: url, method: "GET", contentType: "application/json", dataType: "json"
    });
    request.done(callback);
    request.fail(function () {
        console.log("Failed request to " + url);
    });
}

function printGuestsInTable(tableElement, guests) {
    let jTableElement = $(tableElement);
    let jTbody = jTableElement.find("tbody");
    jTbody.empty();
    for (let guest of guests) {
        if (filterType && !guest.type.toLowerCase().includes(filterType.toLowerCase())) {
            continue;
        }

        if (filterName && !guest.name.toLowerCase().includes(filterName.toLowerCase())) {
            continue;
        }

        if (filterSurname && !guest.surname.toLowerCase().includes(filterSurname.toLowerCase())) {
            continue;
        }

        let tr = trTemplate.clone();
        let input = tr.find("input");
        input.attr("value", guest.id);
        tr.find(".td-id").html(guest.id);
        tr.find(".td-name").html(guest.name);
        tr.find(".td-surname").html(guest.surname);
        tr.find(".td-type").html(guest.type);
        jTbody.append(tr);
    }
}

function resetFields() {
    $("#add-guest-id").val("");
    $("#add-guest-name").val("");
    $("#add-guest-surname").val("");
    $("#add-guest-type").val("").change();
}

function addGuestResultHandler(data) {
    if (data.status) {
        closeModal();
        refreshGuestsTable();
        resetFields();
    } else {
        alert(data.statusMessage);
    }
    console.log(data);
}

function refreshGuestsTable() {
    let table = $("#table-guests");
    getAllGuests((jsonData) => {
        if (jsonData.status) {
            printGuestsInTable(table, jsonData.data);
        }
    });
}

function closeModal() {
    let myModal = bootstrap.Modal.getInstance($("#add-guest-modal"));
    myModal.hide();
}

function actionOnSelected_delete() {
    let idsArray = $("#form-action-on-selected input:checkbox:checked").map(function () {
        return $(this).val();
    }).get();
    for (let id of idsArray) {
        $.ajax({
            url: contextPath + "/rest/guest/delete/" + id, method: "delete", dataType: "json"
        }).done(() => {
            refreshGuestsTable();
        });
    }
}

function setFilters() {
    filterType = $("#filter-select-type").val();
    filterName = $("#filter-input-name").val();
    filterSurname = $("#filter-input-surname").val();
}

$(function () {
    refreshGuestsTable();

    let jFormInsertOne = $("#form-add-guest");

    jFormInsertOne.on("submit", function (event) {
        event.preventDefault();
        createFormAjaxRequest(event.target, addGuestResultHandler)
    });

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
        refreshGuestsTable();
    });
})
