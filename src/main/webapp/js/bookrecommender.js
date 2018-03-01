
$(function() {

    $('#itemLink').click(function () {
        $('#recommenderDiv').hide();
        $('#addItemDiv').show();
    });

    $('#recommenderLink').click(function () {
        $('#addItemDiv').hide();
        $('#recommenderDiv').show();
    });

    $('#recommenderSubmit').click(function(event) {

        event.preventDefault();

        var authorVal = $('#authorQuery').val();
        var yearOfPublicationVal = $('#yearOfPublicationQuery').val();

        if(authorVal !== "" && yearOfPublicationVal != "") {

            $.get('api/recommendation', 
                {
                    author: authorVal, 
                    yearOfPublication: yearOfPublicationVal
                },
                function(data) {

                    var tableRows = '';

                    for(var d in data) {
                        var desc = data[d]._case.description;
                        var result = '<tr>' +
                            '<th scope="row">' + desc.isbn + '</th>' +
                            '<th>' + desc.title + '</th>' +
                            '<th>' + desc.author + '</th>' +
                            '<th>' + desc.yearOfPublication + '</th>' +
                            '<th>' + data[d].eval + '</th>' +
                            '</tr>';

                        tableRows = tableRows + result;
                    }

                    $('#recommendationResultData').empty();
                    $('#recommendationResultData').html(tableRows);

                    $('#recommendationsDiv').show();
                })
            .fail(function() {
                alert("error");
            });
        }
    });

    $('#addItemSubmit').click(function(event) {

        event.preventDefault();

        var isbnVal = $('#isbnItem').val();
        var titleVal = $('#titleItem').val();
        var authorVal = $('#authorItem').val();
        var yearOfPublicationVal = $('#yearOfPublicationItem').val();

        if(isbnVal !== "" && 
            titleVal !== "" && 
            authorVal !== "" && 
            yearOfPublicationVal != "") { 

            $.post('api/addItem',
                {
                    isbn: isbnVal,
                    title: titleVal,
                    author: authorVal, 
                    yearOfPublication: yearOfPublicationVal
                },
                function() {
                    alert("Item saved correctly");
                });

        }

    })

});