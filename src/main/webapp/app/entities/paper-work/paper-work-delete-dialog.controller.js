(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkDeleteController',PaperWorkDeleteController);

    PaperWorkDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaperWork'];

    function PaperWorkDeleteController($uibModalInstance, entity, PaperWork) {
        var vm = this;

        vm.paperWork = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaperWork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
