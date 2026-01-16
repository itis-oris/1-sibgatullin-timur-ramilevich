<#import "layout/base.ftlh" as base>
<@base.layout title="${book.title}">
    <script src="${currentContext}/static/js/favorite.js"> </script>
    <h2>${book.title}</h2>
    <p><b>Автор:</b> <a href="${currentContext}/user?id=${book.author.id}">${book.author.nickname}</a></p>
    <p><b>Жанр:</b> ${book.genre.name}</p>
    <p><b>Описание:</b> ${book.description}</p>
    <#if user??>
        <p><button id="favBtn" class="usual-button"
<#--                   onclick="func()"-->
                data-id="${book.id}"
                data-fav="${book.favorite?string("true", "false")}"
                data-context="${currentContext}">
            ${book.favorite?string("Удалить из избранного", "Добавить в избранное")}
        </button></p>
        <script>favBtn.addEventListener("click", func)</script>
    </#if>

    <a href="${currentContext}/download?id=${book.id}">Скачать книгу</a>
    <p>
        <h3>Комментарии</h3>
        <ul>
            <#list comments as c>
                <li><b>${c.created_at} — <a href="${currentContext}/user?id=${c.user.id}">${c.user.nickname}</a>:</b> ${c.text}</li>
            </#list>
        </ul>
        <form method="post" action="${currentContext}/book">
            <input type="hidden" name="bookId" value="${book.id}">
            <textarea name="comment" rows="3" cols="40" required></textarea><br>
            <button type="submit" class="usual-button">Оставить комментарий</button>
        </form>
    </p>
</@base.layout>
