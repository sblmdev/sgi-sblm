//
//    $(document).ready(function(){
//        var sidebartop = $('#menuflotante').offset().top- parseFloat($('#menuflotante').css('margin-top').replace(/auto/, 0));;
//        $(window).scroll(function(){
//            if( $(window).scrollTop() > sidebartop ) {
//                $('#menuflotante').css({position: 'fixed'});
//            } else {
//                $('#menuflotante').css({position: 'static'});
//            }
//        });
//    });



var name = "#menuflotante";
var menuYloc = null;
 
$(document).ready(function(){
	
	 
	 
    menuYloc = parseInt($(name).css("top").substring(0,$(name).css("top").indexOf("px")));
    $(window).scroll(function () {
        var offset = menuYloc+$(document).scrollTop()+"px";
        $(name).animate({top:offset},{duration:0,queue:false});
    });
});
