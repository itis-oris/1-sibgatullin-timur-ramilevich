function func() {
    const btn = document.getElementById('favBtn');
    const bookId = btn.dataset.id;
    const isFav = btn.dataset.fav === "true";
    const currentContext = btn.dataset.context;

    const url = isFav ? `${currentContext}/favorite/delete` : `${currentContext}/favorite/add`;
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
            body: "id=" + encodeURIComponent(bookId)
    })
    .then(r => r.text())
    .then(resp => {
        if (resp === "OK") {
            if (isFav) {
                btn.dataset.fav = "false";
                btn.innerText = "Добавить в избранное";
            } else {
                btn.dataset.fav = "true";
                btn.innerText = "Удалить из избранного";
            }
        } else {
            alert("Ошибка: " + resp);
        }
    })
    .catch(() => alert("Ошибка AJAX запроса"));
}