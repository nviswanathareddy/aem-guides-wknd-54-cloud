(function (document, $, Granite) {
    "use strict";

    $(document).on("click", ".generate-web-page-id-btn", function (e) {
        e.preventDefault();

        const $button = $(this);
        const field = document.querySelector("input[name='./webPageId']");

        const currentUrl = window.location.href;
        const item = new URL(currentUrl).searchParams.get("item");

        $.ajax({
            url: "/bin/wknd/generate-webpageid",
            type: "GET",
            data: {
                item: item,
            },
            success: function (response) {
                if (field) {
                    field.value = response.webPageId;
                    $(field).trigger("change");
                }

                $button.prop("disabled", true);

                $(window)
                    .adaptTo("foundation-ui")
                    .notify("Success", "Web Page ID generated successfully", "success");

                $("coral-toast").last().addClass("wknd-toast-center");
            },
            error: function (xhr, status, error) {
                console.error("Servlet error:", error);

                $(window)
                    .adaptTo("foundation-ui")
                    .notify("Error", "Failed to generate Web Page ID", "error");

                $("coral-toast").last().addClass("wknd-toast-center");
            },
        });
    });

    $(document).on("foundation-contentloaded", function () {
        const field = document.querySelector("input[name='./webPageId']");
        const button = document.querySelector(".generate-web-page-id-btn");

        if (field) {
            field.readOnly = true;
        }

        if (field && button && field.value.trim()) {
            button.disabled = true;
        }
    });
})(document, Granite.$, Granite);
