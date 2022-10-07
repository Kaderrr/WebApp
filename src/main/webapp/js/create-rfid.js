let trTemplate = $("<tr>" +
    "<td class=no-print'><div class='form-check'><input class='form-check-input text-center' type='checkbox' name='selectedRfids'></div></td>" +
    "<th class='td-code'></th>" +
    "<td class='td-type'></td>" +
    "<td class='td-status'></td>" +
    "</tr>");
let filterStatus;
let filterType;
let rfidsTable = $("#rfids-table");

//let useFilters=false;

function getRfids(callback) {
    let request = $.ajax({
        url: contextPath + "/rest/rfid/all",
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

function closeModal() {
    let myModal = bootstrap.Modal.getInstance($("#modal-insert-rfid"));
    myModal.hide();
}

function printAllRfids(data, table) {
    let tableBody = $(table.find("tbody"));
    tableBody.empty();
    for (let rfid of data) {
        if (filterType && filterType.toUpperCase() !== rfid.type.toUpperCase()) {
            continue;
        }
        if (filterStatus && filterStatus.toUpperCase() !== rfid.status.toUpperCase()) {
            continue;
        }
        let tr = trTemplate.clone();
        let input = tr.find("input");
        input.attr("value", rfid.code);
        tr.find(".td-code").html(rfid.code);
        tr.find(".td-type").html(rfid.type);
        tr.find(".td-status").html(rfid.status);
        tableBody.append(tr);
    }
    //table.css('display', 'inline-block');
}

function getAndPrintRfids(table) {
    getRfids((data) => {
        printAllRfids(data, table);
    });
}

function setFilters() {
    filterStatus = $("#select-filter-status").val();
    filterType = $("#select-filter-type").val();
}

function resetFields() {
    $("#input-rfid-id").val("");
    $("#select-rfid-type").val("").change();
    $("#select-rfid-status").val("").change();
}


function actionOnSelected_delete() {
    let idsArray = $("#form-action-on-selected input:checkbox:checked").map(function () {
        return $(this).val();
    }).get();
    for (let id of idsArray) {
        $.ajax({
            url: contextPath + "/rest/rfid/delete/" + id, method: "delete", dataType: "json"
        }).done(() => {
        });
    }
    getAndPrintRfids(rfidsTable);
}

$(() => {
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

    getAndPrintRfids(rfidsTable);

    $("#btn-filter").on("click", (e) => {
        e.preventDefault();
        setFilters();
        getAndPrintRfids(rfidsTable);
    });

    $("#form-rfid-add").submit((e) => {
        e.preventDefault();
        createFormAjaxRequest(e.target, (result) => {
            if (result.status) {
                getAndPrintRfids(rfidsTable);
                closeModal();
                resetFields();
            } else {
                console.log(result);
            }
        });
    });
});

