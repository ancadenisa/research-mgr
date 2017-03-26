(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkManageUploadsController', PaperWorkManageUploadsController);

    PaperWorkManageUploadsController.$inject = ['$uibModalInstance', 'entity', 'PaperWork', 'Upload', '$timeout', '$http', '$window', 'FileSaver', 'Blob'];
    
    function getFileNameFromHttpResponse(httpResponse) {
        var contentDispositionHeader = httpResponse.headers('Content-Disposition');
        var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];
        return result.replace(/"/g, '');
    }
    
    function b64toBlob(b64Data, contentType, sliceSize) {
    	  contentType = contentType || '';
    	  sliceSize = sliceSize || 512;

    	  var byteCharacters = decodeURIComponent(escape(window.atob(b64Data)));
    	  var byteArrays = [];

    	  for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
    	    var slice = byteCharacters.slice(offset, offset + sliceSize);

    	    var byteNumbers = new Array(slice.length);
    	    for (var i = 0; i < slice.length; i++) {
    	      byteNumbers[i] = slice.charCodeAt(i);
    	    }

    	    var byteArray = new Uint8Array(byteNumbers);

    	    byteArrays.push(byteArray);
    	  }
    	    
    	  var blob = new Blob(byteArrays, {type: contentType});
    	  return blob;
    	}

    function PaperWorkManageUploadsController($uibModalInstance, entity, PaperWork, Upload, $timeout, $http, $window, FileSaver, Blob) {
        var vm = this;
        vm.paperWork = entity;
		
        vm.deletePaperAttachment = function(id) {
			console.log(id);
    	}
		

		vm.downloadPaperAttachment = function(id) {
			$http({
				method : 'GET',
				url : 'api/paperAttachments/download/' + id,
				responseType: "blob"
			}).then(function successCallback(response) {
				console.log(response);
				 var blob = new Blob([response.data], [{type: response.headers('content-type')}]);
				 FileSaver.saveAs(blob, getFileNameFromHttpResponse(response));
			}, function errorCallback(response) {	
			});
		}
		
	}
})();