let result = [];
let baseUrl = window.location.origin;

function inputChange(th){
    $('.number-' + $(th).attr('test-order')).css('background', 'var(--orange)')

    const obj = {
        'test_id' : $(th).attr('test-id'),
        'data_id' : $(th).attr('data-id')
    }

    let isExist = false;
    for (let i = 0; i < result.length; i++) {
        if (result[i]['test-id'] === obj["test-id"]){
            result.splice(i, 1, obj);
            isExist = true;
            break;
        }
    }
    if (!isExist){
        result.push(obj)
    }
}

function checkTest(){
    $.ajax({
        url: baseUrl + "/api/student/rest/check/test",
        type: 'POST',
        enctype: 'application/json',
        data: result,
        processData: false,
        contentType: 'application/json;charset=utf-8',
        cache: false,
        success: function () {
            alert('ok');
        },
        error: function (e) {
            console.log(e);
        }
    })
}