$(document).on('ready page:load', function() {

	// Get a custom event handler 

	// Options for the UI spinner (spin.js in vendor/assets/javascripts)
   var opts = {
     lines: 9, // The number of lines to draw
     length: 4, // The length of each line
     width: 3, // The line thickness
     radius: 6, // The radius of the inner circle
     corners: 1, // Corner roundness (0..1)
     rotate: 0, // The rotation offset
     direction: 1, // 1: clockwise, -1: counterclockwise
     color: '#000', // #rgb or #rrggbb
     speed: 1, // Rounds per second
     trail: 40, // Afterglow percentage
     shadow: false, // Whether to render a shadow
     hwaccel: false, // Whether to use hardware acceleration
     className: 'spinner', // The CSS class to assign to the spinner
     zIndex: 2e9, // The z-index (defaults to 2000000000)
     top: 'auto', // Top position relative to parent in px
     left: '0' // Left position relative to parent in px
   };

    var target = document.getElementById("spin");
    var spinner = new Spinner(opts).spin(target);

});
