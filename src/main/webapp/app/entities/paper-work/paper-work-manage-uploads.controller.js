(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkManageUploadsController', PaperWorkManageUploadsController);

    PaperWorkManageUploadsController.$inject = ['$uibModalInstance', 'entity', 'PaperWork', 'Upload', '$timeout', '$http', '$window', 'FileSaver', 'Blob'];

    function PaperWorkManageUploadsController($uibModalInstance, entity, PaperWork, Upload, $timeout, $http, $window, FileSaver, Blob) {
        var vm = this;
        vm.paperWork = entity;
		
        vm.deletePaperAttachment = function(id) {
			console.log(id);
    	}
		

		vm.downloadPaperAttachment = function(id) {
			$http({
				method : 'GET',
				url : 'api/paperAttachments/download/' + id
			}).then(function successCallback(response) {
				console.log(response);
				 var blob = new Blob([response.data]);
				 FileSaver.saveAs(blob, 'text.pdf');
			}, function errorCallback(response) {
			});
		}
		
	}
})();