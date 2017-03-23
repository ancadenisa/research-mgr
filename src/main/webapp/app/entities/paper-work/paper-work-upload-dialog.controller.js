(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkUploadController', PaperWorkUploadController);

    PaperWorkUploadController.$inject = ['$uibModalInstance', 'entity', 'PaperWork'];

    function PaperWorkUploadController($uibModalInstance, entity, PaperWork) {
        var vm = this;

        vm.paperWork = entity;

    }
})();
