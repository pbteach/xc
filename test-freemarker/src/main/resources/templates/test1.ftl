<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!
<br/>

<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#if stus??>
<#list stus as stu>
    <tr>
        <td <#if (stu.name!"")=="小明">style="background: indianred" </#if>>${stu_index +1}</td>
        <td <#if ((stu.money)!0.0>1000)>style="background: indianred"</#if>>${stu.name!""}</td>
        <td>${stu.age}</td>
        <td>${(stu.money)!0.0}</td>
    </tr>
</#list>
    </#if>
    </table>
<BR/>
遍历map中的学生1:
<br/>
姓名：${(stuMap['stu1'].name)!""}
<br/>
姓名：${(stuMap.stu1.name)!""}
<br/>
遍历map中所有key
<br/>
<#list stuMap?keys as key>
姓名：${stuMap[key].name!""}<br/>
姓名：${stuMap[key].age}
</#list>
</body>
</html>