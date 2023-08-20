<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul>
    <c:forEach var="post" items="${ requestScope.posts }">
        <li>
            <div style="display: flex; flex-direction: column; background: rgba(200, 200, 200, 0.15); padding: 5px; border-radius: 5px; margin: 5px; border: 2px solid rgb(200, 200, 200)">
                <div>
                    <div>
                        <div class="d-flex w-100 justify-content-between">
                            <button class="btn btn-link" onclick="window.location.href='/persons/${ post.creator.id }'">
                                <b>${ post.creator.firstName } ${ post.creator.lastName }</b>
                            </button>
                            <small class="mt-2">${ post.timeAgo }</small>
                        </div>
                    </div>
                    <div class="ml-3">
                        ${ post.content }
                    </div>
                </div>
                <div style="display: flex; flex-direction: column; align-items: flex-end;">
                    <button class="btn btn-warning btn-sm" onclick="window.location.href='/persons/${ param.personId }/posts/${ post.id }'">
                        Kommentieren
                    </button>
                </div>
                <c:if test="${ post.childPosts != null }">
                    <c:set var="posts" value="${ post.childPosts }" scope="request" />
                    <c:import url="/templates/commentTemplate.jsp">
                        <c:param name="personId" value="${ param.personId }" />
                    </c:import>
                </c:if>
            </div>
        </li>
    </c:forEach>
</ul>