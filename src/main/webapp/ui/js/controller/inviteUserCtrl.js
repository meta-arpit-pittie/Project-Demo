var inviteUserPage = angular.module('inviteUserApp', ['ui-notification', 'ngRoute', 'topbarApp', 'sidebarApp']);

inviteUserPage.controller('inviteUserCtrl', function($scope, $http, $window, Notification) {
    $scope.user = {};

    $scope.sendInvite = function() {
        $('#wrapper').hide();
        $('#spinner').show();

        $http({
            method: 'POST',
            url: 'http://localhost:8080/Project-Authentication/user/sendInvitationToUser',
            data: $scope.user,
            headers: {
                'Content-Type': 'application/json'
            }
        }).success(function(response) {
            $('#wrapper').show();
            $('#spinner').hide();
            Notification.info({
                message: "Invitation Send",
                delay: 2000
            });
            setTimeout(function() {
                $window.location.href = 'index.html';
            }, 2500);
        }).error(function(response) {
            $('#wrapper').show();
            $('#spinner').hide();
            Notification.error({
                message: "Couldn't establish connection",
                delay: 2000
            });
        });
    }
});
