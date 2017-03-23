(function() {
    'use strict';
    angular
        .module('researchMgrApp')
        .factory('PaperWork', PaperWork);

    PaperWork.$inject = ['$resource', 'DateUtils'];

    function PaperWork ($resource, DateUtils) {
        var resourceUrl =  'api/paper-works/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.deadlineDate = DateUtils.convertLocalDateFromServer(data.deadlineDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.deadlineDate = DateUtils.convertLocalDateToServer(copy.deadlineDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.deadlineDate = DateUtils.convertLocalDateToServer(copy.deadlineDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
