notes = [];
mode = "Highlight";

$(document).ready(function() {

	// Action Buttons: Highlight/Notes
	$("#summary").css("cursor","text");
	$("#mode button").click(function() {  
		$("#mode button").not(this).removeClass('active');
		$(this).toggleClass('active');
		mode = $(this).text();
		if (mode == "Highlight") {
			$("#summary").css("cursor","text");
		} else {
			$("#summary").css("cursor","crosshair")
		}
	});

	// Initialize highlights
	for (var i = 0; i < highlights.length; i++) {
		$("#sentence" + highlights[i]).addClass("highlighted");
	};
	
	// Handle sentences highlights
	$("#summary > span").mouseup(function() {
		if(mode == "Highlight" && window.getSelection() == 0) {
			$(this).toggleClass(function(index, cl, mode) {
				$(this).children().each(function() {
					$(this).contents().unwrap();
				});
				return "highlighted";
			});
		}
	});

	// Remove all highlights
	$("#removeAll-highlights-button").click(removeAllHighlights = function() {
		$("#summary span").removeClass("highlighted");
		$("#summary span").css("background-color", "");
	});

	// Remove all notes
	$("#removeAll-notes-button").click(removeAllNotes = function() {
		
		for (var i = notes.length - 1; i >= 0; i--) {
			o = notes[i];
			var id = o.sentence;
			$("#note" + o.sentence).remove();
			removeNote(id);
			
		};
	});

	// Initialize user highlighter
	$('#summary').textHighlighter( {
		onBeforeHighlight: function(range) {
			return true;
		}
	});

	// Gather user input highlights	
	$("#save-button").click( save = function() {

		$("#SummaryHtml").val('');
		var ids = [];

		$.each($("#summary .highlighted"), function(i,val) {
			id = $(val).attr("id");
			if(id != undefined) {
				id = id.substr(8);
			} else {
				id = $(val).parent().attr("id").substr(8);
			}
			ids.push(id);

		});
		
		$("#SummaryUserSentences").val(ids.toString());
		if(ids.toString() == '') {
			$("#SummaryUserSentences").val('None');
		}
		$("#SummaryUserNotes").val(JSON.stringify(notes));
		$("#SummaryHtml").html("");

	});

	// Initialize notes
	for (var i = notes.length - 1; i >= 0; i--) {
		var obj = notes[i];
		var offset = $("#sentence" + obj.sentence).offset().top;
		displayNote(notes[i], offset);
	};


	// Initialize bootstrap popover
	$("#summary").popover({ container: '#summary' });
	$("#summary span").click(function() {
		if(mode == "Notes") {
			var id = $(this).attr("id").substr(8);
			var offset = $(this).offset().top;
			
			$("#summary").popover("show");
			if( $("#note" + id).html() != undefined ) {
				$(".popover textarea").val($("#note" + id).html());
			}

			$(".popover").css("top", offset-80 + "px");
			$(".popover textarea").after("<input type='hidden' name='sentence-note-id' value='"+ id +"' />");
			$("#notes-save").click(function() {
				obj = new Object();
				obj.sentence = $(this).prev().prev().val();
				obj.note = $(this).prev().prev().prev().val();
				
				$("#summary").popover("hide");

				displayNote(obj, offset);
				notes.push(obj);
			});	
		}
	});

	// Make the note visible on the right side of the summary
	function displayNote(obj,offset) {
		if( $("#note" + obj.sentence).val() == undefined ) {
			note = "<div class='alert alert-warning note' id='note"+ obj.sentence +"'>"+ obj.note.replace(/\n/g, "<br />") +"<span class='glyphicon glyphicon-remove'></span></div>";
			$("body").append(note);
			$("#note" + obj.sentence).css("left", $("#summary").position().left + 960 + "px");
			$("#note" + obj.sentence).css("top", offset-15);

		} else {
			$("#note" + obj.sentence).html(obj.note.replace(/\n/g, "<br />"));			
		}
	}
	
	// Remove note event
	$( document ).on( "click", ".note .glyphicon-remove", function() {
		id = $(this).parent().attr("id").substring(4);
		removeNote(id);
		$(this).parent().remove();
	});

	// Remove note function
	function removeNote(id) {
		for (var i = notes.length - 1; i >= 0; i--) {
			o = notes[i];
			if (o.sentence == id) {
				notes.splice(i, 1);
			}
		};
	}

	// Save value of the PDF Export options
	$(".options button").click(function() {
		$(this).parent().find("button").not(this).removeClass('active');
		$(this).toggleClass('active');
		type = $(this).parent().attr("id").substring(4);
		if (type == "pdf_type") {
			$("#SummaryPdfType").val($(this).index());
		} else {
			$("#SummaryPdfNotes").val($(this).index());
		}
		
	});

	// PDF Export button event
	$("#export-button").click(function() {
		save();
		generatePdfHtml();
	});

	// Generate PDF for 4 different cases in sent it to the server
	function generatePdfHtml () {
		var pdftype = parseInt($("#SummaryPdfType").val());
		var pdfNotes = parseInt($("#SummaryPdfNotes").val());

		var html1 = $("#summary").html();

		$("#pdf-summary").html(html1);
		$("#pdf-summary span").not(".highlighted").each(function() {
			$(this).next().remove();
			$(this).remove();
		});
		$("#pdf-summary span").removeClass("highlighted");
		$("#pdf-summary span").css("background-color", "");
		var html2 = $("#pdf-summary").html();

		if (pdftype == 0 && pdfNotes == 1) {
			$("#SummaryHtml").val(html1);
		} else if (pdftype == 1 && pdfNotes == 1) {
			$("#pdf-summary").html(html1);
			$("#SummaryHtml").val(html2);
		} else if (pdftype == 0 && pdfNotes == 0) {
			$("#pdf-summary").html(html1);
			addPdfComments();
		} else if (pdftype == 1 && pdfNotes == 0) {
			$("#pdf-summary").html(html2);
			addPdfComments();
		}

		function addPdfComments() {
			for (var i = notes.length - 1; i >= 0; i--) {
				var o = notes[i];
				$("#pdf-summary #sentence" + o.sentence).next().remove();
				$("#pdf-summary #sentence" + o.sentence).after("<p class='pdf-note'>"+ o.note +"</p>");				
			};
			$("#SummaryHtml").val($("#pdf-summary").html());			
		}

		$("#pdf-summary").html("");
		
	}

});