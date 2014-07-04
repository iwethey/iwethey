for i in range(-23, -251, -1):
    print "      INSERT INTO post (post_id, post_forum_id, post_parent_id, post_thread_id, post_subject, post_content, post_content_orig, post_signature, post_signature_orig, post_locked, post_convert_newlines, post_convert_links, post_convert_codes, post_convert_html, post_created_by, post_last_updated_by)\n        values(%d, -2, null, %d, 'Spork Subject %d', 'This is a post about sporks.', 'This is a post about sporks.', 'Spork Sig', 'Spork Sig', 'f', 't', 't', 't', 't', -1, -1);" % (i, i, -i)

