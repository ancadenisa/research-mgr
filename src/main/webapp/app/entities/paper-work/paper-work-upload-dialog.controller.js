(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkUploadController', PaperWorkUploadController);

    PaperWorkUploadController.$inject = ['$uibModalInstance', 'entity', 'PaperWork', 'Upload', '$timeout'];

    function PaperWorkUploadController($uibModalInstance, entity, PaperWork, Upload, $timeout) {
        var vm = this;

        vm.paperWork = entity;
        
        vm.uploadFiles = function (files) {
            vm.files = files;
            if (files && files.length) {
                Upload.upload({
                    url: "api/uploadPaperAttachments/" + vm.paperWork.id,
                    data: {
                        file: files
                    },
                	arrayKey: '',
                	method : 'PUT'
                }).then(function (response) {
                    $timeout(function () {
                        vm.result = response.data;
                    });
                }, function (response) {
                    if (response.status > 0) {
                       vm.errorMsg = response.status + ': ' + response.data;
                    }
                }, function (evt) {
                   vm.progress = 
                        Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
            }
        };
    }
})();
