<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Поиск поездок</h1><hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-5">
                <label for="depCity">Город вылета</label>
                <select id="depCity" name="depCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="Москва" <#if depCity?? && depCity == "Москва">selected</#if>>Москва</option>
                </select>
            </div>

            <div class="form-group col-md-5">
                <label for="arrCity">Город прилета</label>
                <select id="arrCity" name="arrCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="SVO" <#if arrCity?? && arrCity == "SVO">selected</#if>>SVO</option>
                    <option value="DME" <#if arrCity?? && arrCity == "DME">selected</#if>>DME</option>
                    <option value="VKO" <#if arrCity?? && arrCity == "VKO">selected</#if>>VKO</option>

                </select>
            </div>

            <div class="form-group col-md-5">
                <label for="autoClass">Класс авто</label>
                <select id="autoClass" name="autoClass" class="form-control">
                    <option value="">Класс не выбран</option>
                    <option value="Эконом" <#if autoClass?? && autoClass == "Эконом">selected</#if>>Эконом</option>
                    <option value="Комфорт" <#if autoClass?? && autoClass == "Комфорт">selected</#if>>Комфорт</option>
                    <option value="Бизнес" <#if autoClass?? && autoClass == "Бизнес">selected</#if>>Бизнес</option>

                </select>
            </div>

            <button style="width: 100%" type="submit" class="btn btn-warning">Поиск</button>
        </div>
    </form>
    <hr>

    <!-- Grid system with Bootstrap for displaying 3 items per row -->
    <div class="row">
        <#list trips as trip>
            <div class="col-md-4">
                <a href="/trip/${trip.id}?showButton=true" class="trip-link">
                    <div class="card text-white bg-warning my-3 trip">
                        <img src="/images/${trip.previewImageId}" class="card-img-top">
                        <div class="card-body">
                            <h5 class="card-title">${trip.title}</h5>
                            <p class="card-text">${trip.autoClass}      ${trip.price} ₽</p>
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
            </div>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>
</@t.template>
