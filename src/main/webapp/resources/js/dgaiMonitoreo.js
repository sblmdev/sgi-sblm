$(function() {
			
			$("#idExpedienteDetalle").hide();
			
		});	
function mostrarDetalle01() {

			$("#idExpedienteDetalle").show();
	}

function test() {

	 $(".ui-expanded-row-content").bind('click',function(event){
	      event.stopImmediatePropagation();
	      return false;
	  })
	      $(".ui-expanded-row-content").bind('contextmenu',function(event){
	      event.stopImmediatePropagation();
	      return false;
	  })

}

CdwNoticeTables =
{ 
      /**
       * Method will toggle the row expansion when double clicked
       * @param pfWidgetId
       * @param evt
       * @returns {Boolean}
       */
      onDblClick : function( pfWidgetId, evt )
      { 
         var key, rowIndex, pfWidget, activeRow;
         
         // Find the row which was double clicked
         key = "" + evt;
        
         rowIndex = key.substring(key.lastIndexOf('.')+1);
        alert(rowIndex);
         pfWidget = window[pfWidgetId];
         alert("doble3");
         activeRow = pfWidget.tbody.children("tr:eq(" + rowIndex + ")");
        
//         activeRow
//            // Jump to the column holding the row toggler
//            .children("td:eq(1)")
//            
//            // Find the row toggler and click it
//            .find(".ui-row-toggler")
//            .trigger("click");
        
         activeRow.children("td:eq(1)").find(".ui-row-toggler").trigger("click");
         
         // Return false to stop AJAX operations on double click
         return false;
      }
}