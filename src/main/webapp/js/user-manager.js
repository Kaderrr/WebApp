let trTemplate = $("<tr>" +
    "<td class=\"no-print\">" +
    "<div class=\"form-check\">" +
    "<input class=\"form-check-input\" type=\"checkbox\" name=\"selectedUsers\">" +
    "</div>" + "</td>" + "<th class=\"td-id\" scope=\"row\"></th>" +
    "<td class=\"td-email\"></td>" +
    "<td class=\"td-is-active\"></td>" +
    "<td class=\"td-perm-level\"></td>" +
    "</tr>");

let filterType;

function getAllUsers(callback) {
    let url = contextPath + "/rest/user/all";
    request = $.ajax({
        url: url, method: "GET", contentType: "application/json", dataType: "json"
    });
    request.done(callback);
    request.fail(function () {
        console.log("Failed request to " + url);
    });
}

function printUsersInTable(tableElement, users) {
    let jTableElement = $(tableElement);
    let jTbody = jTableElement.find("tbody");
    jTbody.empty();
    for (let user of users) {
        if (filterType && !user.permission_level.toLowerCase().includes(filterType.toLowerCase())) {
            continue;
        }
        let tr = trTemplate.clone();
        let input = tr.find("input");
        input.attr("value", user.id);
        tr.find(".td-id").html(user.id);
        tr.find(".td-email").html(user.email);
        let isActive = ((user.isActive == true) ? "Yes" : "No");
        tr.find(".td-is-active").html(isActive);

        let permission;
        switch (user.permission_level) {
            case "admin":
                permission = "Admin"; break;
            case "service_provider":
                permission = "Service Provider"; break;
            case "receptionist":
                permission = "Receptionist"; break;
        }
        tr.find(".td-perm-level").html(permission);
        jTbody.append(tr);
    }
}

function resetFields() {
    $("#add-user-id").val("");
    $("#add-user-email").val("");
    $("#add-user-password").val("");
    $("#add-user-type").val("").change();
    $("#add-user-is-active").val(false);
    $("#add-user-is-active").prop('checked', false);
}

function addUserResultHandler(data) {
    if (data.status) {
        closeModal();
        refreshUsersTable();
        resetFields();
    } else {
        alert(data.statusMessage);
    }
    console.log(data);
}

function refreshUsersTable() {
    let table = $("#table-users");
    getAllUsers((jsonData) => {
        if (jsonData.status) {
            printUsersInTable(table, jsonData.data);
        }
    });
}

function closeModal() {
    let myModal = bootstrap.Modal.getInstance($("#add-user-modal"));
    myModal.hide();
}

function actionOnSelected_delete() {
    let idsArray = $("#form-action-on-selected input:checkbox:checked").map(function () {
        return $(this).val();
    }).get();
    for (let id of idsArray) {
        $.ajax({
            url: contextPath + "/rest/user/delete/" + id, method: "delete", dataType: "json"
        }).done(() => {
            refreshUsersTable();
        });
    }
}

$(function () {
    refreshUsersTable();

    let jFormInsertOne = $("#form-add-user");

    jFormInsertOne.on("submit", function (event) {
        event.preventDefault();
        createFormAjaxRequest(event.target, addUserResultHandler)
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
        filterType = $("#filter-select-type").val();
        refreshUsersTable();
    });

    $("#add-user-is-active").val(false)
    $("#add-user-is-active").on("change", (e) => {
        currentVal = $("#add-user-is-active").val() == "true";
        $("#add-user-is-active").val(!currentVal)
    });
})
