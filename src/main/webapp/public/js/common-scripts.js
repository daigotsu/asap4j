var UUI = function () {


    $(".menu-custom ul li").find("ul").parent('li').addClass("has-submenu");
    $('.left-menu .has-submenu > a').on('click', function (e) {

        e.preventDefault();

        $(this).next('.left-menu ul').find('> li').slideToggle();

        if ($(this).parent("li").parent("ul").hasClass(".left-menu ul") && $(".left-menu ul > li > a").hasClass("nav-open")) {
            $(".left-menu ul > li > a.nav-open").next('ul').find('> ').slideToggle();
            $(".left-menu ul > li > a.nav-open").removeClass("nav-open");
        }

        $(this).toggleClass('nav-open');
    });

    $(".header-menu ul li").hover(function () {
        $(this).addClass("hover");
        $('ul:first', this).css('display', 'block');
    }, function () {
        $(this).removeClass("hover");
        $('ul:first', this).css('display', 'none');
    });


//    sidebar toggle


    /*$(function () {
        function responsiveView() {
            var wSize = $(window).width();
            if (wSize <= 768) {
                $('#container').addClass('sidebar-close');
                $('#sidebar-left').hide();
            }

            if (wSize > 768) {
                $('#container').removeClass('sidebar-close');
                $('#sidebar-left').show();
            }
        }

        $(window).on('load', responsiveView);
        $(window).on('resize', responsiveView);
    });


    $('.sidebar-left-toggle').click(function () {

        var hidden = $('#sidebar-left');
        var content = $('#main-content');
        if (hidden.hasClass('visible')) {
            hidden.hide().removeClass('visible');
            content.animate({"margin-left": "0px"}, 0);
        } else {
            hidden.show().addClass('visible');
            content.animate({"margin-left": "250px"}, 0);
        }

    });*/

    // custom scrollbar

    require(['jquery.nicescroll.min'], function () {
        $(".sidebar-left-inner").niceScroll({styler: "fb", cursorcolor: "#dadada", cursorwidth: '3', cursorborderradius: '0', background: '', cursorborder: ''});

        $("html").niceScroll({styler: "fb", cursorcolor: "#dadada", cursorwidth: '6', cursorborderradius: '0', background: '', cursorborder: '', zindex: '1000'});
    });


}();