(function() {
    'use strict';

    angular
        .module('researchMgrApp')
        .controller('PaperWorkDetailController', PaperWorkDetailController);

    PaperWorkDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaperWork', 'User'];

    function PaperWorkDetailController($scope, $rootScope, $stateParams, previousState, entity, PaperWork, User) {
        var vm = this;

        vm.paperWork = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('researchMgrApp:paperWorkUpdate', function(event, result) {
            vm.paperWork = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
