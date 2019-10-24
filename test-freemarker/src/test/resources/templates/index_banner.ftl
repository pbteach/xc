<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div class="banner-roll">
    <div class="banner-item">
        <#if banner??>
            <#list banner.cmsConfig.model as model>
                <div class="item" style="background-image: url(${(model.value)!""});"></div>
            </#list>

        </#if>
        <#--<div class="item" style="background-image: url(../img/widget-bannerB.jpg);"></div>
        <div class="item" style="background-image: url(../img/widget-bannerA.jpg);"></div>
        <div class="item" style="background-image: url(../img/widget-banner3.png);"></div>
        <div class="item" style="background-image: url(../img/widget-bannerB.jpg);"></div>
        <div class="item" style="background-image: url(../img/widget-bannerA.jpg);"></div>
        <div class="item" style="background-image: url(../img/widget-banner3.png);"></div>-->
    </div>
    <div class="indicators"></div>
</div>
</body>
</html>