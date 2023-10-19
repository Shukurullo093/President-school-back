let result = [];
let baseUrl = window.location.origin;

function inputChange(th){
    $('.number-' + $(th).attr('test-order')).css('background', 'var(--orange)');

    const obj = {
        'test_id' : $(th).attr('test-id'),
        'data_id' : $(th).attr('data-id')
    }
    console.log(obj.data_id)
    console.log(obj.test_id)
    let isExist = false;

    result.forEach((product) => {
        if (product.test_id === obj.test_id){
            isExist = true;
            product = obj;
        }
    });

    if (!isExist){
        console.log('ok')
        result.push(obj)
    }
}

function checkTest(){
    if (result.length <= 18){
        let trueAns = 0;
        for (let i = 0; i < 3; i++){
            if (result[i]['data_id'] === '1'){
                trueAns++;
            }
        }
        $.ajax({
            url: baseUrl + "/api/student/rest/check/test/" + window.location.href.toString().split('/')[-1],
            type: 'POST',
            enctype: 'application/json',
            data: JSON.stringify(trueAns),
            processData: false,
            contentType: 'application/json;charset=utf-8',
            cache: false,
            success: function () {
                $('#success-finish').css('display', 'block');
                $('#ul').css('display', 'none');
            },
            error: function (e) {
                console.log(e);
            }
        })
    }
}