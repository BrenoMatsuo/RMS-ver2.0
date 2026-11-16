"use strict";

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("form[data-confirm]").forEach(function (form) {
        form.addEventListener("submit", function (event) {
            if (!window.confirm(form.getAttribute("data-confirm"))) {
                event.preventDefault();
            }
        });
    });

    var imageUrl = document.querySelector("[data-image-url]");
    var imageFile = document.querySelector("[data-image-file]");
    var imagePreview = document.querySelector("[data-image-preview]");
    var previewObjectUrl;
    if (imageUrl && imageFile && imagePreview) {
        function updateImagePreview() {
            if (previewObjectUrl) {
                URL.revokeObjectURL(previewObjectUrl);
                previewObjectUrl = null;
            }
            var file = imageFile.files && imageFile.files[0];
            var source = file ? (previewObjectUrl = URL.createObjectURL(file)) : imageUrl.value.trim();
            if (source) {
                imagePreview.src = source;
                imagePreview.classList.remove("hidden");
            } else {
                imagePreview.removeAttribute("src");
                imagePreview.classList.add("hidden");
            }
        }
        imageUrl.addEventListener("input", updateImagePreview);
        imageFile.addEventListener("change", updateImagePreview);
        updateImagePreview();
    }

    var printButton = document.querySelector("[data-print-invoice]");
    if (printButton) {
        printButton.addEventListener("click", function () {
            window.print();
            var message = document.getElementById("print-message");
            if (message) {
                message.classList.remove("hidden");
            }
        });
    }
});
