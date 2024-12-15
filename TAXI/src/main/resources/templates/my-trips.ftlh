<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Мои поездки</h1>
    <hr>
    <#list user.roles as role>
        <#if role == "ROLE_ADMIN">
            <a class="btn btn-warning mt-3 mb-3" style="text-align: center; width: 100%"
               data-toggle="collapse" href="#collapseExample" role="button"
               aria-expanded="true" aria-controls="collapseExample">
                Добавить новую поездку
            </a>
            <div class="collapse mb-3" id="collapseExample" style="">
                <form action="/trip/create" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="tripName">Название</label>
                        <input type="text" required class="form-control"
                               id="tripName" name="title">
                    </div>
                    <div class="form-group">
                        <label for="price">Цена поездки</label>
                        <div class="input-group mb-3">
                            <input type="number" required class="form-control" id="price" name="price">
                            <div class="input-group-append">
                                <span class="input-group-text">₽</span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="searchDepCity">Город вылета</label>
                        <select id="searchDepCity" required name="depCity" class="form-control">
                            <option value="">Город не выбран</option>

                            <option value="Москва">Москва</option>

                        </select>
                    </div>
                    <div class="form-group">
                        <label for="searchArrCity">Город прилета</label>
                        <select id="searchArrCity" required name="arrCity" class="form-control">
                            <option value="">Город не выбран</option>

                            <option value="SVO">SVO</option>
                            <option value="DME">DME</option>
                            <option value="VKO">VKO</option>


                        </select>
                    </div>

                    <div class="form-group">
                        <label for="autoClass">Класс авто</label>
                        <select id="autoClass" required name="autoClass" class="form-control">
                            <option value="">Город не выбран</option>
                            <option value="Эконом">Эконом</option>
                            <option value="Комфорт">Комфорт</option>
                            <option value="Бизнес">Бизнес</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="description">Описание</label>
                        <textarea class="form-control" id="description" name="description"></textarea>
                    </div>

                    <div class="form-group">
                        <div class="custom-file">
                            <input type="file" name="file1" id="file1">
                            <label class="custom-file-label" for="file1">Фотография 1</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="custom-file">
                            <input type="file" name="file2" id="file2">
                            <label class="custom-file-label" for="file2">Фотография 2</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="custom-file">
                            <input type="file" name="file3" id="file3">
                            <label class="custom-file-label" for="file3">Фотография 3</label>
                        </div>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <button type="submit" style="width: 100%" class="btn btn-warning">Добавить</button>
                </form>
            </div>
        </#if>
    </#list>
    <div class="card-columns">
        <#list trips as trip>
            <a href="/trip/${trip.id}?showButton=false" class="trip-link">
                <div class="card text-white bg-warning my-3 trip">
                    <div class="card-top">
                        <form action="/trip/delete/${trip.id}" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger m-3">
                                Удалить
                            </button>
                        </form>
                    </div>
                    <img src="/images/${trip.previewImageId}"
                         class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${trip.title}</h5>
                        <p class="card-text">~${trip.price} ₽</p>
                    </div>
                        <div class="card-footer">
                            <#if trip.depCity?? && trip.arrCity??>
                                ${trip.depCity} - ${trip.arrCity}
                            <#else>
                                Город не указан
                            </#if>
                        </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Пока поездок нет!</h1>
        </#list>
    </div>
</@t.template>