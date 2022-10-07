function insertElement(query, element, autoDelete, setActive, hideLogout) {
    element.load(query, function () {
        console.log("Element " + query + " loaded");
        if (setActive) {
            element.find(setActive).addClass("active");
            console.log("Found something to set active")
        }
        if (hideLogout) {
            element.find(".logout-button").css("visibility", "hidden");
        }
        element.contents().unwrap();
        console.log("Deleted element");
    });
}

$(function () {
    $("*").each(function (index, element) {
        let data = $(element).data("autoload");
        let autoDelete = $(element).data("autodelete");
        let setActive = $(element).data("setactive");
        let hideLogout = $(element).data("hidelogout");
        if (data) {
            insertElement(data, $(element), autoDelete, setActive, hideLogout);
        }
    })
});
