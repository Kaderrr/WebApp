let contextPath = $("#context-path").html();

function printTransactions(table, data) {
    if (data.length === 0) {
        table.css("display", "none");
        return;
    }
    let tbody = $(table.find("tbody"));
    tbody.empty();
    $.each(data, function (i, item) {
        let tr = $('<tr>').append(
            $('<td>').text(item.id),
            $('<td>').text(item.amount),
            $('<td>').text(item.datetime),
            $('<td>').text(item.description),
            $('<td>').text(item.booking_id),
            $('<td>').text(item.state),
            $('<td>').text(item.type),
            $('<td>').text(item.user_id),
        );
        tbody.append(tr);
    });
    table.css("display", "table");
}

$(function () {
    let form = $("#booking-id-form");
    let table = $("#transactions-table");

    form.on("submit", (event) => {
        event.preventDefault();
        createFormAjaxRequest(
            form,
            (result) => {
                if (!result.status) {
                    console.log(result);
                    return;
                }
                printTransactions(table, result.data);
            }
        )
    });
});
