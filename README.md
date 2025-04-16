# java-explore-with-me
Template repository for ExploreWithMe project.


Реализация Доп. функции Комментарий к событию
1) для Public API
- Возможность получения комментариев для определенного события (/events/{eventId}/comments)
2) Для Private
- Получить все свои комментарии (/users/#{userId}/comments)
- Возможность добавить комментарий (/users/#{userId}/events/{eventId}/comments/)
- Возможность редактировать сущ. комментарий (/users/#{userId}/events/{eventId}/comments/#{commentId})
- Удалить комментарий (/users/#{userId}/comments/#{commentId})
3) Для Admin
- Получить любой комментарий (admin/comments/#{commentId})
- Удалить любой комментарий (admin/comments/#{commentId})