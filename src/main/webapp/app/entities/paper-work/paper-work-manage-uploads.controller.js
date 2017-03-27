(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkManageUploadsController', PaperWorkManageUploadsController);

    PaperWorkManageUploadsController.$inject = ['$uibModalInstance', 'entity', 'PaperWork', 'Upload', '$timeout', '$http', '$window', 'FileSaver', 'Blob', 'PaperAttachment'];
    
    function getFileNameFromHttpResponse(httpResponse) {
        var contentDispositionHeader = httpResponse.headers('Content-Disposition');
        var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];
        return result.replace(/"/g, '');
    }

    function PaperWorkManageUploadsController($uibModalInstance, entity, PaperWork, Upload, $timeout, $http, $window, FileSaver, Blob, PaperAttachment) {
        var vm = this;
        vm.paperWork = entity;
		
        vm.deletePaperAttachment = function(id) {
        	// TODO ANCA: it should be done with delete like is done for paper-work
        	PaperAttachment.delete({id:id}, function () {
            	vm.paperWork.paperAttachments = vm.paperWork.paperAttachments.filter(function( obj ) {
            	    return obj.id !== id;
            	});            
        	});
    	}
		
		vm.downloadPaperAttachment = function(id) {
			$http({
				method : 'GET',
				url : 'api/paperAttachments/download/' + id,
				responseType: "blob"
			}).then(function successCallback(response) {
				console.log(response);
				 var blob = new Blob([response.data]);
				 FileSaver.saveAs(blob, getFileNameFromHttpResponse(response));
			}, function errorCallback(response) {	
			});
		}
		
	}
})();