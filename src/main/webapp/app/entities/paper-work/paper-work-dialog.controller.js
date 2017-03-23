(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkDialogController', PaperWorkDialogController);

    PaperWorkDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaperWork', 'User'];

    function PaperWorkDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaperWork, User) {
        var vm = this;

        vm.paperWork = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paperWork.id !== null) {
                PaperWork.update(vm.paperWork, onSaveSuccess, onSaveError);
            } else {
                PaperWork.save(vm.paperWork, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('researchMgrApp:paperWorkUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.deadlineDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
